package com.app.sabrosometro.model;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * Una fila de la tabla "restaurante".
 *
 *   id_restaurante       SERIAL PRIMARY KEY
 *   id_dueno             INTEGER → usuario(id_usuario)  (puede ser NULL)
 *   nombre               VARCHAR(120) NOT NULL
 *   tipo_comida          VARCHAR(60)  NOT NULL
 *   direccion            VARCHAR(200) NOT NULL
 *   zona                 VARCHAR(60)  NOT NULL
 *   telefono             VARCHAR(20)        (puede ser NULL)
 *   hora_apertura        TIME NOT NULL
 *   hora_cierre          TIME NOT NULL
 *   promedio_general     NUMERIC(3,2) NOT NULL, entre 0 y 5
 *   total_calificaciones INTEGER NOT NULL, >= 0
 *
 * Integer (no int) en idDueno: así puede valer null, igual que en la tabla.
 * Promedio en BigDecimal: NUMERIC es exacto y double no.
 */

public class Restaurante {
    private int        idRestaurante;
    private Integer    idDueno;              // columna id_dueno (opcional)
    private String     nombre;
    private String     tipoComida;           // columna tipo_comida
    private String     direccion;
    private String     zona;
    private String     telefono;             // puede ser NULL
    private LocalTime  horaApertura;         // columna hora_apertura
    private LocalTime  horaCierre;           // columna hora_cierre
    private BigDecimal promedioGeneral;      // columna promedio_general
    private int        totalCalificaciones;  // columna total_calificaciones

    public Restaurante() { }

    public int getIdRestaurante()                        { return idRestaurante; }
    public void setIdRestaurante(int idRestaurante)      { this.idRestaurante = idRestaurante; }

    public Integer getIdDueno()                          { return idDueno; }
    public void setIdDueno(Integer idDueno)              { this.idDueno = idDueno; }

    public String getNombre()                            { return nombre; }
    public void setNombre(String nombre)                 { this.nombre = nombre; }

    public String getTipoComida()                        { return tipoComida; }
    public void setTipoComida(String tipoComida)         { this.tipoComida = tipoComida; }

    public String getDireccion()                         { return direccion; }
    public void setDireccion(String direccion)           { this.direccion = direccion; }

    public String getZona()                              { return zona; }
    public void setZona(String zona)                     { this.zona = zona; }

    public String getTelefono()                          { return telefono; }
    public void setTelefono(String telefono)             { this.telefono = telefono; }

    public LocalTime getHoraApertura()                   { return horaApertura; }
    public void setHoraApertura(LocalTime horaApertura)  { this.horaApertura = horaApertura; }

    public LocalTime getHoraCierre()                     { return horaCierre; }
    public void setHoraCierre(LocalTime horaCierre)      { this.horaCierre = horaCierre; }

    public BigDecimal getPromedioGeneral()                       { return promedioGeneral; }
    public void setPromedioGeneral(BigDecimal promedioGeneral)   { this.promedioGeneral = promedioGeneral; }

    public int getTotalCalificaciones()                          { return totalCalificaciones; }
    public void setTotalCalificaciones(int totalCalificaciones)  { this.totalCalificaciones = totalCalificaciones; }
}
