package com.app.sabrosometro.model;

import java.time.LocalDateTime;

// CREATE TABLE favorito (
//     id_favorito     SERIAL PRIMARY KEY,
//     id_usuario      INT NOT NULL REFERENCES usuario (id_usuario)
//                     ON DELETE CASCADE,                                     -- RN-10
//     id_restaurante  INT NOT NULL REFERENCES restaurante (id_restaurante)
//                     ON DELETE CASCADE,                                     -- RN-10
//     tipo_lista      VARCHAR(12) NOT NULL DEFAULT 'favorito'
//                     CONSTRAINT ck_favorito_tipo CHECK (tipo_lista IN ('favorito', 'por_visitar')),
//     fecha           TIMESTAMP NOT NULL DEFAULT now(),
//     CONSTRAINT uq_favorito_usuario_rest UNIQUE (id_usuario, id_restaurante) -- RN-11
// );
public class Favorito {
    private int idFavorito;
    private int idUsuario;
    private int idRestaurante;
    private String tipoLista;
    private LocalDateTime fecha;

    public Favorito(){}

    public int getIdFavorito() {
        return idFavorito;
    }
    public void setIdFavorito(int idFavorito) {
        this.idFavorito = idFavorito;
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
    public String getTipoLista() {
        return tipoLista;
    }
    public void setTipoLista(String tipoLista) {
        this.tipoLista = tipoLista;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }


}
