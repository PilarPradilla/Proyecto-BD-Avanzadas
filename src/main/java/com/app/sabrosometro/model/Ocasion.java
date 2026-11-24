package com.app.sabrosometro.model;

// CREATE TABLE ocasion (
//     id_ocasion  SERIAL PRIMARY KEY,                                        -- RN-01
//     nombre      VARCHAR(60) NOT NULL UNIQUE
// );
public class Ocasion {
    private int idOcasion;
    private String nombre;

    public Ocasion(){}

    public int getIdOcasion() {
        return idOcasion;
    }

    public void setIdOcasion(int idOcasion) {
        this.idOcasion = idOcasion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
