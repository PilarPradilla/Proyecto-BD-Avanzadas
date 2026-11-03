package com.app.sabrosometro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.app.sabrosometro.model.Plato;

/** CRUD de la tabla "plato" con JdbcTemplate: aquí vive TODO el SQL. */
@Repository
public class PlatoRepository {
    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public PlatoRepository(JdbcTemplate jdbc, NamedParameterJdbcTemplate namedJdbc) {
        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    private static final String COLUMNAS =
            "id_plato, id_restaurante, nombre, descripcion, precio, id_usuario_modifico";

    private static final RowMapper<Plato> MAPPER = (rs, rowNum) -> {
        Plato p = new Plato();
        p.setIdPlato(rs.getInt("id_plato"));
        p.setIdRestaurante(rs.getInt("id_restaurante"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecio(rs.getBigDecimal("precio"));
        p.setIdUsuarioModifico(rs.getObject("id_usuario_modifico", Integer.class)); // puede ser NULL
        return p;
    };

    // ── READ ───────────────────────────────────────────────────────────

    public List<Plato> findAll() {
        return jdbc.query("SELECT " + COLUMNAS + " FROM plato ORDER BY id_plato", MAPPER);
    }

    public Optional<Plato> findById(int id) {
        return jdbc.query("SELECT " + COLUMNAS + " FROM plato WHERE id_plato = ?", MAPPER, id)
                   .stream()
                   .findFirst();
    }

    public List<Plato> findByRestaurante(int idRestaurante) {
        return jdbc.query("SELECT " + COLUMNAS
                        + " FROM plato WHERE id_restaurante = ? ORDER BY nombre",
                          MAPPER, idRestaurante);
    }

    // ── CREATE ─────────────────────────────────────────────────────────

    public Plato save(Plato plato) {
        String sql = """
                INSERT INTO plato (id_restaurante, nombre, descripcion, precio, id_usuario_modifico)
                VALUES (:idRestaurante, :nombre, :descripcion, :precio, :idUsuarioModifico)
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("idRestaurante",    plato.getIdRestaurante())
                .addValue("nombre",           plato.getNombre())
                .addValue("descripcion",      plato.getDescripcion())
                .addValue("precio",           plato.getPrecio())
                .addValue("idUsuarioModifico", plato.getIdUsuarioModifico());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbc.update(sql, params, keyHolder, new String[]{"id_plato"});
        plato.setIdPlato(keyHolder.getKey().intValue());
        return plato;
    }

    // ── UPDATE ─────────────────────────────────────────────────────────

    public boolean update(int id, Plato plato) {
        String sql = """
                UPDATE plato
                   SET id_restaurante = :idRestaurante, nombre = :nombre,
                       descripcion = :descripcion, precio = :precio,
                       id_usuario_modifico = :idUsuarioModifico
                 WHERE id_plato = :id
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("idRestaurante",    plato.getIdRestaurante())
                .addValue("nombre",           plato.getNombre())
                .addValue("descripcion",      plato.getDescripcion())
                .addValue("precio",           plato.getPrecio())
                .addValue("idUsuarioModifico", plato.getIdUsuarioModifico())
                .addValue("id",               id);
        return namedJdbc.update(sql, params) > 0;
    }

    // ── DELETE ─────────────────────────────────────────────────────────

    public boolean delete(int id) {
        return jdbc.update("DELETE FROM plato WHERE id_plato = ?", id) > 0;
    }
}
