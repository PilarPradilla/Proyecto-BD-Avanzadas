package com.app.sabrosometro.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.app.sabrosometro.model.CalificacionPlato;

@Repository
public class CalificacionPlatoRepository {
    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public CalificacionPlatoRepository(JdbcTemplate jdbc, NamedParameterJdbcTemplate namedJdbc) {
        this.jdbc = jdbc; this.namedJdbc = namedJdbc;
    }

    private static final String COLUMNAS = "id_calificacion_plato, id_usuario, id_plato, fecha, puntuacion, comentario";

    private static final RowMapper<CalificacionPlato> MAPPER = (rs, n) -> {
        CalificacionPlato c = new CalificacionPlato();
        c.setIdCalificacionPlato(rs.getInt("id_calificacion_plato"));
        c.setIdUsuario(rs.getInt("id_usuario"));
        c.setIdPlato(rs.getInt("id_plato"));
        c.setFecha(rs.getObject("fecha", LocalDate.class));
        c.setPuntuacion(rs.getInt("puntuacion"));
        c.setComentario(rs.getString("comentario"));
        return c;
    };

    public List<CalificacionPlato> findAll() {
        return jdbc.query("SELECT " + COLUMNAS + " FROM calificacion_plato ORDER BY id_calificacion_plato", MAPPER);
    }

    public Optional<CalificacionPlato> findById(int id) {
        return jdbc.query("SELECT " + COLUMNAS + " FROM calificacion_plato WHERE id_calificacion_plato = ?", MAPPER, id)
                   .stream().findFirst();
    }

    public List<CalificacionPlato> findByPlato(int idPlato) {
        return jdbc.query("SELECT " + COLUMNAS + " FROM calificacion_plato WHERE id_plato = ? ORDER BY fecha DESC",
                          MAPPER, idPlato);
    }

    public CalificacionPlato insert(CalificacionPlato c) {
        KeyHolder kh = new GeneratedKeyHolder();
        namedJdbc.update(
            "INSERT INTO calificacion_plato (id_usuario, id_plato, puntuacion, comentario) VALUES (:u, :p, :pt, :c)",
            new MapSqlParameterSource("u", c.getIdUsuario())
                .addValue("p", c.getIdPlato())
                .addValue("pt", c.getPuntuacion())
                .addValue("c", c.getComentario()),
            kh, new String[] { "id_calificacion_plato" });
        return findById(kh.getKey().intValue()).orElseThrow();
    }

    public int update(int id, CalificacionPlato c) {
        return jdbc.update("UPDATE calificacion_plato SET puntuacion = ?, comentario = ? WHERE id_calificacion_plato = ?",
                           c.getPuntuacion(), c.getComentario(), id);
    }

    public int delete(int id) {
        return jdbc.update("DELETE FROM calificacion_plato WHERE id_calificacion_plato = ?", id);
    }
}