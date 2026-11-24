package com.app.sabrosometro.model;

import java.time.LocalDateTime;

// CREATE TABLE historial_precio_plato (
//     id_historial     SERIAL PRIMARY KEY,
//     id_plato         INT NOT NULL REFERENCES plato (id_plato) ON DELETE CASCADE,
//     id_usuario       INT NOT NULL REFERENCES usuario (id_usuario) ON DELETE RESTRICT,
//     precio_anterior  NUMERIC(10,2) NOT NULL,
//     precio_nuevo     NUMERIC(10,2) NOT NULL,
//     fecha            TIMESTAMP NOT NULL DEFAULT now()
// );
public class HistorialPrecioPlato {
    private int idHistorial;
    private int idPlato;
    private int idUsuario;
    private java.math.BigDecimal precioAnterior;
    private java.math.BigDecimal precioNuevo;
    private LocalDateTime fecha;

    public HistorialPrecioPlato(){}

    public int getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(int idHistorial) {
        this.idHistorial = idHistorial;
    }

    public int getIdPlato() {
        return idPlato;
    }

    public void setIdPlato(int idPlato) {
        this.idPlato = idPlato;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public java.math.BigDecimal getPrecioAnterior() {
        return precioAnterior;
    }

    public void setPrecioAnterior(java.math.BigDecimal precioAnterior) {
        this.precioAnterior = precioAnterior;
    }

    public java.math.BigDecimal getPrecioNuevo() {
        return precioNuevo;
    }

    public void setPrecioNuevo(java.math.BigDecimal precioNuevo) {
        this.precioNuevo = precioNuevo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    
}
