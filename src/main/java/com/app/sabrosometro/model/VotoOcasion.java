package com.app.sabrosometro.model;

import java.time.LocalDateTime;

// CREATE TABLE voto_ocasion (
//     id_usuario      INT NOT NULL REFERENCES usuario (id_usuario) ON DELETE RESTRICT,
//     id_restaurante  INT NOT NULL REFERENCES restaurante (id_restaurante) ON DELETE CASCADE,
//     id_ocasion      INT NOT NULL REFERENCES ocasion (id_ocasion) ON DELETE CASCADE,
//     fecha           TIMESTAMP NOT NULL DEFAULT now(),
//     PRIMARY KEY (id_usuario, id_restaurante, id_ocasion)   -- un voto por usuario, restaurante y ocasión
// );
public class VotoOcasion {
    private int idUsuario;
    private int idRestaurante;
    private int idOcasion;
    private LocalDateTime fecha;

    public VotoOcasion(){}

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
    public int getIdOcasion() {
        return idOcasion;
    }
    public void setIdOcasion(int idOcasion) {
        this.idOcasion = idOcasion;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
