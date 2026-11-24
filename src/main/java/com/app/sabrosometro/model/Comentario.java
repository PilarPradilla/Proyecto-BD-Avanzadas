package com.app.sabrosometro.model;

import java.time.LocalDateTime;

// CREATE TABLE comentario (
//     id_comentario   SERIAL PRIMARY KEY,                                    -- RN-01
//     id_usuario      INT NOT NULL REFERENCES usuario (id_usuario)
//                     ON DELETE RESTRICT,                                    -- RN-10
//     id_restaurante  INT NOT NULL REFERENCES restaurante (id_restaurante)
//                     ON DELETE RESTRICT,                                    -- RN-10, RN-12
//     texto           TEXT NOT NULL,
//     fecha           TIMESTAMP NOT NULL DEFAULT now(),
//     visible         BOOLEAN NOT NULL DEFAULT true
// );
public class Comentario {
    private int idComentario;
    private int idUsuario;
    private int idRestaurante;
    private String texto;
    private LocalDateTime fecha;
    private boolean visible;

    public Comentario(){}

    public int getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(int idComentario) {
        this.idComentario = idComentario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(int idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
}