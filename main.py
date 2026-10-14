import os
from urllib.parse import quote_plus
from dotenv import load_dotenv
load_dotenv()

import bcrypt
import psycopg2
import psycopg2.errors
import psycopg2.extras
from fastapi import FastAPI, Request, Form, HTTPException, Depends
from fastapi.responses import RedirectResponse
from fastapi.templating import Jinja2Templates
from starlette.middleware.sessions import SessionMiddleware

app = FastAPI()

# Firma la cookie de sesión. En producción ponla en una variable de entorno.
app.add_middleware(
    SessionMiddleware,
    secret_key=os.getenv("SECRET_KEY", "cambia-esto-por-algo-largo-y-aleatorio"),
)


def contexto(request: Request):
    """Variables disponibles en TODOS los HTML sin tener que enviarlas."""
    return {
        "usuario": request.session.get("nombre"),
        "is_admin": request.session.get("rol") == "administrador",
    }


templates = Jinja2Templates(directory="templates", context_processors=[contexto])


def obtener_conexion():
    return psycopg2.connect(
        host=os.getenv("DB_HOST"),
        port=os.getenv("DB_PORT"),
        dbname=os.getenv("DB_NAME"),
        user=os.getenv("DB_USER"),
        password=os.getenv("DB_PASSWORD"),
        options="-c lc_messages=C",
    )

def volver(url_base: str, error: str = ""):
    url = url_base
    if error:
        url += "?error=" + quote_plus(error)
    return RedirectResponse(url=url, status_code=303)


def requerir_admin(request: Request):
    if request.session.get("rol") != "administrador":
        raise HTTPException(status_code=403, detail="Solo administradores")

# PAGINA INDEX
# requerir un login para ingresar
def requerir_login(request: Request):
    if not request.session.get("id_usuario"):
        # 303 + Location = redirige a la bienvenida
        raise HTTPException(status_code=303, headers={"Location": "/"})


# REGISTRO 
@app.get("/registro")
def registro_form(request: Request, error: str = ""):
    return templates.TemplateResponse(request, "registro.html", {"error": error, "active": ""})

# se añade un usuario a la base de datos
@app.post("/registro")
def registro(
    nombre: str = Form(...),
    correo: str = Form(...),
    contrasena: str = Form(...),
):
    if len(contrasena) < 6:
        return volver("/registro", "La contraseña debe tener al menos 6 caracteres")

    hash_ = bcrypt.hashpw(contrasena.encode("utf-8"), bcrypt.gensalt()).decode("utf-8")

    conexion = obtener_conexion()
    try:
        cursor = conexion.cursor()
        cursor.execute(
            "INSERT INTO usuario (nombre, correo, contrasena) VALUES (%s, %s, %s);",
            (nombre, correo.strip().lower(), hash_),
        )
        conexion.commit()
        cursor.close()
    except psycopg2.errors.UniqueViolation:
        conexion.rollback()
        return volver("/registro", "Ese correo ya está registrado")
    except psycopg2.errors.CheckViolation:
        conexion.rollback()
        return volver("/registro", "El correo no tiene un formato válido")
    except psycopg2.Error:
        conexion.rollback()
        return volver("/registro", "No se pudo crear la cuenta")
    finally:
        conexion.close()

    return volver("/login")


# ---------- LOGIN ----------
@app.get("/login")
def login_form(request: Request, error: str = ""):
    return templates.TemplateResponse(request, "login.html", {"error": error, "active": ""})


@app.post("/login")
def login(request: Request, correo: str = Form(...), contrasena: str = Form(...)):
    conexion = obtener_conexion()
    try:
        cursor = conexion.cursor(cursor_factory=psycopg2.extras.RealDictCursor)
        cursor.execute(
            "SELECT id_usuario, nombre, contrasena, rol FROM usuario WHERE correo = %s;",
            (correo.strip().lower(),),
        )
        u = cursor.fetchone()
        cursor.close()
    finally:
        conexion.close()

    if u is None or not bcrypt.checkpw(
        contrasena.encode("utf-8"), u["contrasena"].encode("utf-8")
    ):
        # Mismo mensaje en ambos casos, para no revelar si el correo existe
        return volver("/login", "Correo o contraseña incorrectos")

    request.session["id_usuario"] = u["id_usuario"]
    request.session["nombre"] = u["nombre"]
    request.session["rol"] = u["rol"]
    return RedirectResponse(url="/", status_code=303)


# LOGOUT
@app.post("/logout")
def logout(request: Request):
    request.session.clear()
    return RedirectResponse(url="/", status_code=303)

# INICIO
@app.get("/")
def inicio(request: Request):
    if not request.session.get("id_usuario"):
        return templates.TemplateResponse(request, "index.html", {"active": ""})
    return templates.TemplateResponse(request, "inicio.html", {"active": "inicio"})


# PERFIL
@app.get("/perfil/{id_usuario}", dependencies=[Depends(requerir_login)])
def inicio(request: Request, id_usuario: int, admin: bool = False, error: str = ""):
    conexion = obtener_conexion()
    try:
        cursor = conexion.cursor(cursor_factory=psycopg2.extras.RealDictCursor)
        cursor.execute("SELECT * FROM usuario WHERE id_usuario = %s;", (id_usuario,))
        usuario = cursor.fetchall()
        if usuario is None:
            raise HTTPException(status_code=404, detail="Usuario no encontrado")
        cursor.close()
    finally:
        conexion.close()        
    
    return templates.TemplateResponse(request, "perfil.html", {
        "active": "perfil",
        "usuario": usuario,
    })

# RESTAURANTES
@app.get("/restaurantes", dependencies=[Depends(requerir_login)])
def listar_restaurantes(request: Request, admin: bool = False, error: str = ""):
    conexion = obtener_conexion()
    try:
        cursor = conexion.cursor(cursor_factory=psycopg2.extras.RealDictCursor)
        cursor.execute("SELECT id_restaurante, nombre, tipo_comida, direccion, precio_promedio  FROM restaurante ORDER BY nombre;")
        restaurantes = cursor.fetchall()
        cursor.close()
    finally:
        conexion.close()

    return templates.TemplateResponse(request, "restaurantes.html", {
        "restaurantes": restaurantes,
        "is_admin": admin,
        "error": error,
        "active": "restaurantes",
    })


# FILTROS (para el panel del buscador)
@app.get("/api/filtros", dependencies=[Depends(requerir_login)])
def obtener_filtros():
    conexion = obtener_conexion()
    try:
        cursor = conexion.cursor()
        cursor.execute(
            """
            SELECT DISTINCT tipo_comida
            FROM restaurante
            WHERE tipo_comida IS NOT NULL
            ORDER BY tipo_comida;
            """
        )
        tipos = [fila[0] for fila in cursor.fetchall()]
        cursor.close()
    finally:
        conexion.close()

    return {"Tipo de comida": tipos}


# INFO RESTAURANTE
@app.get("/restaurante/{id_restaurante}", dependencies=[Depends(requerir_login)])
def detalle_restaurante(
    request: Request, id_restaurante: int, admin: bool = False, error: str = ""
):
    conexion = obtener_conexion()
    try:
        cursor = conexion.cursor(cursor_factory=psycopg2.extras.RealDictCursor)
        cursor.execute("SELECT * FROM restaurante WHERE id_restaurante = %s;", (id_restaurante,))
        restaurante = cursor.fetchone()
        if restaurante is None:
            raise HTTPException(status_code=404, detail="Restaurante no encontrado")

        cursor.execute(
            "SELECT * FROM plato WHERE id_restaurante = %s ORDER BY nombre;",
            (id_restaurante,),
        )
        platos = cursor.fetchall()
        cursor.close()
    finally:
        conexion.close()

    return templates.TemplateResponse(request, "restaurante.html", {
        "r": restaurante,
        "platos": platos,
        "is_admin": admin,
        "error": error,
        "active": "restaurantes",
    })


# FAVARITOS
@app.get("/favoritos", dependencies=[Depends(requerir_login)])
def restaurantes_favoritos(request: Request, admin: bool = False, error: str = ""):
    conexion = obtener_conexion()
    try:
        cursor = conexion.cursor(cursor_factory=psycopg2.extras.RealDictCursor)
        cursor.execute("SELECT id_restaurante, nombre, tipo_comida, direccion, precio_promedio  FROM restaurante ORDER BY nombre;")
        restaurantes = cursor.fetchall()
        cursor.close()
    finally:
        conexion.close()

    return templates.TemplateResponse(request, "favoritos.html", {
        "restaurantes": restaurantes,
        "is_admin": admin,
        "error": error,
        "active": "favoritos",
    })


# ADMINISTRADOR
# AGREGAR RESTAURANTE 
@app.post("/agregar", dependencies=[Depends(requerir_admin)])
def agregar_restaurante(
    nombre: str = Form(...),
    tipo_comida: str = Form(...),
    direccion: str = Form(...),
    hora_apertura: str = Form(...),
    hora_cierre: str = Form(...),
    telefono: str = Form(""),
    precio_promedio: str = Form(""),
):
    conexion = obtener_conexion()
    try:
        cursor = conexion.cursor()
        cursor.execute(
            """
            INSERT INTO restaurante
                (nombre, tipo_comida, direccion, telefono,
                 hora_apertura, hora_cierre, precio_promedio)
            VALUES (%s, %s, %s, %s, %s, %s, %s);
            """,
            (nombre, tipo_comida, direccion, telefono or None,
             hora_apertura, hora_cierre, precio_promedio or None),
        )
        conexion.commit()
        cursor.close()
    except psycopg2.Error:
        conexion.rollback()
        return volver("/restaurantes", "No se pudo guardar el restaurante (¿ya existe o el precio es negativo?)")
    finally:
        conexion.close()

    return volver("/restaurantes")


# AGREGAR PLATO
@app.post("/agregar_plato", dependencies=[Depends(requerir_admin)])
def agregar_plato(
    id_restaurante: int = Form(...),
    nombre: str = Form(...),
    precio: str = Form(...),
    descripcion: str = Form(""),
):
    destino = f"/restaurante/{id_restaurante}"
    conexion = obtener_conexion()
    try:
        cursor = conexion.cursor()
        cursor.execute(
            """
            INSERT INTO plato (id_restaurante, nombre, descripcion, precio)
            VALUES (%s, %s, %s, %s);
            """,
            (id_restaurante, nombre, descripcion or None, precio),
        )
        conexion.commit()
        cursor.close()
    except psycopg2.Error:
        conexion.rollback()
        return volver(destino, "No se pudo guardar el plato (¿ya existe en este restaurante o el precio es negativo?)")
    finally:
        conexion.close()

    return volver(destino)

# EDITAR RESTAURANTE
@app.post("/editar_restaurante/{id_restaurante}", dependencies=[Depends(requerir_admin)])
def editar_restaurante(
    id_restaurante: int,
    nombre: str = Form(...),
    tipo_comida: str = Form(...),
    direccion: str = Form(...),
    hora_apertura: str = Form(...),
    hora_cierre: str = Form(...),
    telefono: str = Form(""),
    precio_promedio: str = Form(""),
):
    destino = f"/restaurante/{id_restaurante}"
    conexion = obtener_conexion()
    try:
        cursor = conexion.cursor()
        cursor.execute(
            """
            UPDATE restaurante
            SET nombre = %s,
                tipo_comida = %s,
                direccion = %s,
                telefono = %s,
                hora_apertura = %s,
                hora_cierre = %s,
                precio_promedio = %s
            WHERE id_restaurante = %s;
            """,
            (nombre, tipo_comida, direccion, telefono or None,
             hora_apertura, hora_cierre, precio_promedio or None,
             id_restaurante),
        )
        conexion.commit()
        cursor.close()
    except psycopg2.Error:
        conexion.rollback()
        return volver(destino, "No se pudo actualizar el restaurante (¿ya existe ese nombre o el precio es negativo?)")
    finally:
        conexion.close()

    return volver(destino)

# ELIMINAR RESTAURANTE
@app.post("/eliminar_restaurante/{id_restaurante}", dependencies=[Depends(requerir_admin)])
def editar_restaurante(
    id_restaurante: int,
):
    destino = f"/restaurante/{id_restaurante}"
    conexion = obtener_conexion()
    try:
        cursor = conexion.cursor()
        cursor.execute(
            """
            DELETE FROM restaurante
            WHERE id_restaurante = %s;
            """,
            (id_restaurante,),
        )
        conexion.commit()
        cursor.close()
    except psycopg2.Error:
        conexion.rollback()
        return volver(f"/restaurantes/{id_restaurante}", "No se pudo eliminar el restaurante")
    finally:
        conexion.close()

    return volver("/restaurantes")