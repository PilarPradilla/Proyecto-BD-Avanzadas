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

import com.app.sabrosometro.model.CalificacionRestaurante;

@Repository
public class CalificacionRestauranteRepository {
    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public CalificacionRestauranteRepository(JdbcTemplate jdbc, NamedParameterJdbcTemplate namedJdbc) {
        this.jdbc = jdbc; this.namedJdbc = namedJdbc;
    }

    private static final String COLUMNAS =
        "id_calificacion_restaurante, id_usuario, id_restaurante, fecha, sabor, atencion_cliente, ambiente, limpieza";

    private static final RowMapper<CalificacionRestaurante> MAPPER = (rs, n) -> {
        CalificacionRestaurante c = new CalificacionRestaurante();
        c.setIdCalificacionRestaurante(rs.getInt("id_calificacion_restaurante"));
        c.setIdUsuario(rs.getInt("id_usuario"));
        c.setIdRestaurante(rs.getInt("id_restaurante"));
        c.setFecha(rs.getObject("fecha", LocalDate.class));
        c.setSabor(rs.getInt("sabor"));
        c.setAtencionCliente(rs.getInt("atencion_cliente"));
        c.setAmbiente(rs.getInt("ambiente"));
        c.setLimpieza(rs.getInt("limpieza"));
        return c;
    };

    public List<CalificacionRestaurante> findAll() {
        return jdbc.query("SELECT " + COLUMNAS + " FROM calificacion_restaurante ORDER BY id_calificacion_restaurante", MAPPER);
    }

    public Optional<CalificacionRestaurante> findById(int id) {
        return jdbc.query("SELECT " + COLUMNAS + " FROM calificacion_restaurante WHERE id_calificacion_restaurante = ?",
                          MAPPER, id).stream().findFirst();
    }

    public List<CalificacionRestaurante> findByRestaurante(int idRestaurante) {
        return jdbc.query("SELECT " + COLUMNAS + " FROM calificacion_restaurante WHERE id_restaurante = ? ORDER BY fecha DESC",
                          MAPPER, idRestaurante);
    }

    public CalificacionRestaurante insert(CalificacionRestaurante c) {
        KeyHolder kh = new GeneratedKeyHolder();
        namedJdbc.update(
            "INSERT INTO calificacion_restaurante (id_usuario, id_restaurante, sabor, atencion_cliente, ambiente, limpieza) " +
            "VALUES (:u, :r, :s, :a, :am, :l)",
            new MapSqlParameterSource("u", c.getIdUsuario())
                .addValue("r", c.getIdRestaurante())
                .addValue("s", c.getSabor())
                .addValue("a", c.getAtencionCliente())
                .addValue("am", c.getAmbiente())
                .addValue("l", c.getLimpieza()),
            kh, new String[] { "id_calificacion_restaurante" });
        return findById(kh.getKey().intValue()).orElseThrow();
    }

    public int update(int id, CalificacionRestaurante c) {
        return jdbc.update(
            "UPDATE calificacion_restaurante SET sabor = ?, atencion_cliente = ?, ambiente = ?, limpieza = ? " +
            "WHERE id_calificacion_restaurante = ?",
            c.getSabor(), c.getAtencionCliente(), c.getAmbiente(), c.getLimpieza(), id);
    }

    public int delete(int id) {
        return jdbc.update("DELETE FROM calificacion_restaurante WHERE id_calificacion_restaurante = ?", id);
    }
}