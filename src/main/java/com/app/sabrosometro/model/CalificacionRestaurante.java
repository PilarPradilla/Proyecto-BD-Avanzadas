package com.app.sabrosometro.model;

import java.time.LocalDate;

// CREATE TABLE calificacion_restaurante (
//     id_calificacion_restaurante SERIAL PRIMARY KEY,                        -- RN-01
//     id_usuario        INT NOT NULL REFERENCES usuario (id_usuario)
//                       ON DELETE RESTRICT,                                  -- RN-10
//     id_restaurante    INT NOT NULL REFERENCES restaurante (id_restaurante)
//                       ON DELETE RESTRICT,                                  -- RN-10, RN-12
//     fecha             DATE NOT NULL DEFAULT CURRENT_DATE,
//     sabor             SMALLINT NOT NULL CONSTRAINT ck_calrest_sabor
//                       CHECK (sabor BETWEEN 1 AND 5),                       -- RN-08
//     atencion_cliente  SMALLINT NOT NULL CONSTRAINT ck_calrest_atencion
//                       CHECK (atencion_cliente BETWEEN 1 AND 5),            -- RN-08
//     ambiente          SMALLINT NOT NULL CONSTRAINT ck_calrest_ambiente
//                       CHECK (ambiente BETWEEN 1 AND 5),                    -- RN-08
//     limpieza          SMALLINT NOT NULL CONSTRAINT ck_calrest_limpieza
// );
public class CalificacionRestaurante {
    private int idCalificacionRestaurante;
    private int idUsuario;
    private int idRestaurante;
    private LocalDate fecha;
    private int sabor;
    private int atencionCliente;
    private int ambiente;
    private int limpieza;

    public CalificacionRestaurante(){}

    public int getIdCalificacionRestaurante() {
        return idCalificacionRestaurante;
    }

    public void setIdCalificacionRestaurante(int idCalificacionRestaurante) {
        this.idCalificacionRestaurante = idCalificacionRestaurante;
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getSabor() {
        return sabor;
    }

    public void setSabor(int sabor) {
        this.sabor = sabor;
    }

    public int getAtencionCliente() {
        return atencionCliente;
    }

    public void setAtencionCliente(int atencionCliente) {
        this.atencionCliente = atencionCliente;
    }

    public int getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(int ambiente) {
        this.ambiente = ambiente;
    }

    public int getLimpieza() {
        return limpieza;
    }

    public void setLimpieza(int limpieza) {
        this.limpieza = limpieza;
    }

}
