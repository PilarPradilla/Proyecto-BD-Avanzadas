package com.app.sabrosometro.repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.app.sabrosometro.model.Favorito;


@Repository
public class FavoritoRepository {
    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public FavoritoRepository(JdbcTemplate jdbc, NamedParameterJdbcTemplate namedJdbc) {
        this.jdbc = jdbc; this.namedJdbc = namedJdbc;
    }

    private static final String COLUMNAS = "id_favorito, id_usuario, id_restaurante, tipo_lista, fecha";

    private static final RowMapper<Favorito> MAPPER = (rs, n) -> {
        Favorito f = new Favorito();
        f.setIdFavorito(rs.getInt("id_favorito"));
        f.setIdUsuario(rs.getInt("id_usuario"));
        f.setIdRestaurante(rs.getInt("id_restaurante"));
        f.setTipoLista(rs.getString("tipo_lista"));
        f.setFecha(rs.getObject("fecha", LocalDateTime.class));
        return f;
    };

    public List<Favorito> findAll() {
        return jdbc.query("SELECT " + COLUMNAS + " FROM favorito ORDER BY id_favorito", MAPPER);
    }

    public Optional<Favorito> findById(int id) {
        return jdbc.query("SELECT " + COLUMNAS + " FROM favorito WHERE id_favorito = ?", MAPPER, id)
                   .stream().findFirst();
    }

    public List<Favorito> findByUsuario(int idUsuario) {
        return jdbc.query("SELECT " + COLUMNAS + " FROM favorito WHERE id_usuario = ? ORDER BY fecha DESC",
                          MAPPER, idUsuario);
    }

    public Favorito insert(Favorito f) {
        KeyHolder kh = new GeneratedKeyHolder();
        namedJdbc.update(
            "INSERT INTO favorito (id_usuario, id_restaurante, tipo_lista) VALUES (:u, :r, :t)",
            new MapSqlParameterSource("u", f.getIdUsuario())
                .addValue("r", f.getIdRestaurante())
                .addValue("t", f.getTipoLista() == null ? "favorito" : f.getTipoLista()),
            kh, new String[] { "id_favorito" });
        return findById(kh.getKey().intValue()).orElseThrow();
    }

    public int update(int id, Favorito f) {
        return jdbc.update("UPDATE favorito SET tipo_lista = ? WHERE id_favorito = ?", f.getTipoLista(), id);
    }

    public int delete(int id) {
        return jdbc.update("DELETE FROM favorito WHERE id_favorito = ?", id);
    }
}