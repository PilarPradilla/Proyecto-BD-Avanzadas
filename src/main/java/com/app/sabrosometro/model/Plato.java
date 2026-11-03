package com.app.sabrosometro.model;

// CREATE TABLE plato (
//     id_plato            SERIAL PRIMARY KEY,                                -- RN-01
//     id_restaurante      INT NOT NULL REFERENCES restaurante (id_restaurante)
//                         ON DELETE RESTRICT,                                -- RN-06, RN-12
//     nombre              VARCHAR(120) NOT NULL,
//     descripcion         TEXT,
//     precio              NUMERIC(10,2) NOT NULL
//                         CONSTRAINT ck_plato_precio CHECK (precio > 0),     -- RN-07
//     id_usuario_modifico INT REFERENCES usuario (id_usuario) ON DELETE RESTRICT,
//     CONSTRAINT uq_plato_nombre UNIQUE (id_restaurante, nombre)
// );
public class Plato {
    private int idPlato;
    private int idRestaurante;
    private String nombre;
    private String descripcion;
    private java.math.BigDecimal precio;
    private Integer idUsuarioModifico;

    public Plato() { }

    public int getIdPlato() { return idPlato; }
    public void setIdPlato(int idPlato) { this.idPlato = idPlato; }

    public int getIdRestaurante() { return idRestaurante; }
    public void setIdRestaurante(int idRestaurante) { this.idRestaurante = idRestaurante; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public java.math.BigDecimal getPrecio() { return precio; }
    public void setPrecio(java.math.BigDecimal precio) { this.precio = precio; }

    public Integer getIdUsuarioModifico()                          { return idUsuarioModifico; }
    public void setIdUsuarioModifico(Integer idUsuarioModifico)    { this.idUsuarioModifico = idUsuarioModifico; }

}
