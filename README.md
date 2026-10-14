# Proyecto Bases de Datos Avanzadas - Sabrosometro

Aplicación web para gestionar restaurantes y platos, con registro, inicio de sesión y panel de administración.

## Tecnologías

Python, FastAPI, PostgreSQL, Jinja2, Tailwind CSS.

## Requisitos

- Python 3.10 o superior
- PostgreSQL
- Git

## Instalación

1. Clonar el proyecto:

```powershell
git clone https://github.com/USUARIO/proyecto-bd-avanzadas.git
cd proyecto-bd-avanzadas
```

2. Crear y activar un entorno virtual:

```powershell
python -m venv venv
.\venv\Scripts\Activate.ps1
```

3. Instalar las dependencias:

```powershell
pip install -r requirements.txt
```

## Configuración

1. Crear una base de datos en PostgreSQL con las tablas `usuario`, `restaurante` y `plato`.

2. Copiar el archivo de ejemplo de variables de entorno:

```powershell
copy .env.example .env
```

3. Abrir `.env` y completar con los datos de tu PostgreSQL y una `SECRET_KEY` propia. Para generar una:

```powershell
python -c "import secrets; print(secrets.token_hex(32))"
```

## Ejecutar

```powershell
uvicorn main:app --reload
```

Abrir en el navegador: http://127.0.0.1:8000

## Funcionalidades

- Registro e inicio de sesión (contraseñas protegidas con `bcrypt`).
- Acceso solo para usuarios registrados.
- Listado de restaurantes con búsqueda y filtro por tipo de comida.
- Detalle de restaurante con sus platos.
- Administradores: crear, editar y eliminar restaurantes, y añadir platos.

## Usuarios administradores

Un usuario es administrador si tiene el valor `administrador` en la columna `rol` de la tabla `usuario`.

## Estructura

```text
├── main.py
├── requirements.txt
├── .env.example
├── README.md
└── templates/
    ├── base.html
    ├── bienvenida.html
    ├── inicio.html
    ├── login.html
    ├── registro.html
    ├── restaurante.html
    └── restaurantes.html
```

## Seguridad

No subas el archivo `.env` a GitHub: contiene tus contraseñas.