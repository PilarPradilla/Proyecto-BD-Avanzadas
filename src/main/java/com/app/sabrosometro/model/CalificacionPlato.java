package com.app.sabrosometro.model;

import java.time.LocalDate;

// CREATE TABLE calificacion_plato (
//     id_calificacion_plato SERIAL PRIMARY KEY,                              -- RN-01
//     id_usuario   INT NOT NULL REFERENCES usuario (id_usuario)
//                  ON DELETE RESTRICT,                                       -- RN-10
//     id_plato     INT NOT NULL REFERENCES plato (id_plato)
//                  ON DELETE CASCADE,                                        -- RN-10
//     fecha        DATE NOT NULL DEFAULT CURRENT_DATE,
//     puntuacion   SMALLINT NOT NULL CONSTRAINT ck_calplato_puntuacion
//                  CHECK (puntuacion BETWEEN 1 AND 5),                       -- RN-09
//     comentario   TEXT,
//     CONSTRAINT uq_calplato_usuario_plato UNIQUE (id_usuario, id_plato)     -- AJUSTE (ver nota arriba)
// );
public class CalificacionPlato {
    private int idCalificacionPlato;
    private int idUsuario;
    private int idPlato;
    private LocalDate fecha;
    private int puntuacion;
    private String comentario;

    public CalificacionPlato(){}

    public int getIdCalificacionPlato() {
        return idCalificacionPlato;
    }

    public void setIdCalificacionPlato(int idCalificacionPlato) {
        this.idCalificacionPlato = idCalificacionPlato;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdPlato() {
        return idPlato;
    }

    public void setIdPlato(int idPlato) {
        this.idPlato = idPlato;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

}
