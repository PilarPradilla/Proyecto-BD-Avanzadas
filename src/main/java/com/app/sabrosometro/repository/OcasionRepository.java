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

import com.app.sabrosometro.model.Ocasion;

@Repository
public class OcasionRepository {
    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public OcasionRepository(JdbcTemplate jdbc, NamedParameterJdbcTemplate namedJdbc) {
        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    private static final String COLUMNAS = "id_ocasion, nombre";

    private static final RowMapper<Ocasion> MAPPER = (rs, rowNum) -> {
        Ocasion o = new Ocasion();
        o.setIdOcasion(rs.getInt("id_ocasion"));
        o.setNombre(rs.getString("nombre"));
        return o;
    };

    public List<Ocasion> findAll() {
        return jdbc.query("SELECT " + COLUMNAS + " FROM ocasion ORDER BY id_ocasion", MAPPER);
    }

    public Optional<Ocasion> findById(int id) {
        return jdbc.query("SELECT " + COLUMNAS + " FROM ocasion WHERE id_ocasion = ?", MAPPER, id)
                   .stream().findFirst();
    }

    public Ocasion insert(Ocasion o) {
        KeyHolder kh = new GeneratedKeyHolder();
        namedJdbc.update("INSERT INTO ocasion (nombre) VALUES (:nombre)",
                new MapSqlParameterSource("nombre", o.getNombre()),
                kh, new String[] { "id_ocasion" });
        o.setIdOcasion(kh.getKey().intValue());
        return o;
    }

    /** @return filas afectadas (0 = no existe) */
    public int update(int id, Ocasion o) {
        return jdbc.update("UPDATE ocasion SET nombre = ? WHERE id_ocasion = ?", o.getNombre(), id);
    }

    /** @return filas afectadas (0 = no existe) */
    public int delete(int id) {
        return jdbc.update("DELETE FROM ocasion WHERE id_ocasion = ?", id);
    }
}