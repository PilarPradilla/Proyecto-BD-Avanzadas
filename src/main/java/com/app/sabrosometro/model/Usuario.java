package com.app.sabrosometro.model;

import java.time.LocalDate;

// CREATE TABLE usuario (
//     id_usuario       SERIAL PRIMARY KEY,                                   -- RN-01
//     nombre           VARCHAR(100) NOT NULL,
//     correo           VARCHAR(150) NOT NULL UNIQUE,                         -- RN-02
//     contrasena_hash  VARCHAR(255) NOT NULL,                                -- RNF-01 (nunca texto plano)
//     rol              VARCHAR(15)  NOT NULL DEFAULT 'cliente'               -- RN-05
//                      CONSTRAINT ck_usuario_rol
//                      CHECK (rol IN ('cliente', 'dueño', 'administrador')), -- RN-03
//     estado           VARCHAR(12)  NOT NULL DEFAULT 'activo'                -- RN-05
//                      CONSTRAINT ck_usuario_estado
//                      CHECK (estado IN ('activo', 'suspendido')),           -- RN-04
//     fecha_registro   DATE NOT NULL DEFAULT CURRENT_DATE,
//     CONSTRAINT ck_usuario_correo CHECK (correo LIKE '%_@_%._%')
// );
public class Usuario {
    private int idUsuario;
    private String nombre;
    private String correo;
    private String contrasenaHash;
    private String rol;
    private String estado;
    private LocalDate fechaRegistro;

    public Usuario() { }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasenaHash() { return contrasenaHash; }
    public void setContrasenaHash(String contrasenaHash) { this.contrasenaHash = contrasenaHash; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDate getFechaRegistro()                      { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro)    { this.fechaRegistro = fechaRegistro; }

}
