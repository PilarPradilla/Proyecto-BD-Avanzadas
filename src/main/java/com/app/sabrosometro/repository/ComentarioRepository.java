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

import com.app.sabrosometro.model.Comentario;

@Repository
public class ComentarioRepository {
    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public ComentarioRepository(JdbcTemplate jdbc, NamedParameterJdbcTemplate namedJdbc) {
        this.jdbc = jdbc; this.namedJdbc = namedJdbc;
    }

    private static final String COLUMNAS = "id_comentario, id_usuario, id_restaurante, texto, fecha, visible";

    private static final RowMapper<Comentario> MAPPER = (rs, n) -> {
        Comentario c = new Comentario();
        c.setIdComentario(rs.getInt("id_comentario"));
        c.setIdUsuario(rs.getInt("id_usuario"));
        c.setIdRestaurante(rs.getInt("id_restaurante"));
        c.setTexto(rs.getString("texto"));
        c.setFecha(rs.getObject("fecha", LocalDateTime.class));
        c.setVisible(rs.getBoolean("visible"));
        return c;
    };

    public List<Comentario> findAll() {
        return jdbc.query("SELECT " + COLUMNAS + " FROM comentario ORDER BY fecha DESC", MAPPER);
    }

    public Optional<Comentario> findById(int id) {
        return jdbc.query("SELECT " + COLUMNAS + " FROM comentario WHERE id_comentario = ?", MAPPER, id)
                   .stream().findFirst();
    }

    /** Solo comentarios visibles (los moderados no se muestran) */
    public List<Comentario> findByRestaurante(int idRestaurante) {
        return jdbc.query("SELECT " + COLUMNAS + " FROM comentario WHERE id_restaurante = ? AND visible = true ORDER BY fecha DESC",
                          MAPPER, idRestaurante);
    }

    public Comentario insert(Comentario c) {
        KeyHolder kh = new GeneratedKeyHolder();
        namedJdbc.update(
            "INSERT INTO comentario (id_usuario, id_restaurante, texto) VALUES (:u, :r, :t)",
            new MapSqlParameterSource("u", c.getIdUsuario())
                .addValue("r", c.getIdRestaurante())
                .addValue("t", c.getTexto()),
            kh, new String[] { "id_comentario" });
        return findById(kh.getKey().intValue()).orElseThrow();
    }

    /** Edita texto y visibilidad (moderación) */
    public int update(int id, Comentario c) {
        return jdbc.update("UPDATE comentario SET texto = ?, visible = ? WHERE id_comentario = ?",
                           c.getTexto(), c.isVisible(), id);
    }

    public int delete(int id) {
        return jdbc.update("DELETE FROM comentario WHERE id_comentario = ?", id);
    }
}