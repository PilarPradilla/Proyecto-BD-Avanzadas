package com.app.sabrosometro.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.app.sabrosometro.model.HistorialPrecioPlato;

@Repository
public class HistorialPrecioPlatoRepository {
    private final JdbcTemplate jdbc;

    public HistorialPrecioPlatoRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    private static final String COLUMNAS =
        "id_historial, id_plato, id_usuario, precio_anterior, precio_nuevo, fecha";

    private static final RowMapper<HistorialPrecioPlato> MAPPER = (rs, n) -> {
        HistorialPrecioPlato h = new HistorialPrecioPlato();
        h.setIdHistorial(rs.getInt("id_historial"));
        h.setIdPlato(rs.getInt("id_plato"));
        h.setIdUsuario(rs.getInt("id_usuario"));
        h.setPrecioAnterior(rs.getBigDecimal("precio_anterior"));
        h.setPrecioNuevo(rs.getBigDecimal("precio_nuevo"));
        h.setFecha(rs.getObject("fecha", LocalDateTime.class));
        return h;
    };

    public List<HistorialPrecioPlato> findAll() {
        return jdbc.query("SELECT " + COLUMNAS + " FROM historial_precio_plato ORDER BY fecha DESC", MAPPER);
    }

    public Optional<HistorialPrecioPlato> findById(int id) {
        return jdbc.query("SELECT " + COLUMNAS + " FROM historial_precio_plato WHERE id_historial = ?",
                          MAPPER, id).stream().findFirst();
    }

    public List<HistorialPrecioPlato> findByPlato(int idPlato) {
        return jdbc.query("SELECT " + COLUMNAS + " FROM historial_precio_plato WHERE id_plato = ? ORDER BY fecha DESC",
                          MAPPER, idPlato);
    }
}