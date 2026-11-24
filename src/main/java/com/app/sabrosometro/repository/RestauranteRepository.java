package com.app.sabrosometro.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.app.sabrosometro.model.Restaurante;

@Repository
public class RestauranteRepository {
    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public RestauranteRepository(JdbcTemplate jdbc, NamedParameterJdbcTemplate namedJdbc) {
        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    private static final String COLUMNAS =
            "id_restaurante, id_dueno, nombre, tipo_comida, direccion, zona, telefono, "
          + "hora_apertura, hora_cierre, promedio_general, total_calificaciones";

    private static final RowMapper<Restaurante> MAPPER = (rs, rowNum) -> {
        Restaurante r = new Restaurante();
        r.setIdRestaurante(rs.getInt("id_restaurante"));
        r.setIdDueno(rs.getObject("id_dueno", Integer.class));  // getInt convertiría NULL en 0
        r.setNombre(rs.getString("nombre"));
        r.setTipoComida(rs.getString("tipo_comida"));
        r.setDireccion(rs.getString("direccion"));
        r.setZona(rs.getString("zona"));
        r.setTelefono(rs.getString("telefono"));
        r.setHoraApertura(rs.getObject("hora_apertura", LocalTime.class));
        r.setHoraCierre(rs.getObject("hora_cierre", LocalTime.class));
        r.setPromedioGeneral(rs.getBigDecimal("promedio_general"));
        r.setTotalCalificaciones(rs.getInt("total_calificaciones"));
        return r;
    };

    // ── READ ───────────────────────────────────────────────────────────

    public List<Restaurante> findAll() {
        return jdbc.query("SELECT " + COLUMNAS + " FROM restaurante ORDER BY id_restaurante", MAPPER);
    }

    public Optional<Restaurante> findById(int id) {
        return jdbc.query("SELECT " + COLUMNAS + " FROM restaurante WHERE id_restaurante = ?",
                          MAPPER, id)
                   .stream()
                   .findFirst();
    }

    public List<Restaurante> findByZona(String zona) {
        return jdbc.query("SELECT " + COLUMNAS
                        + " FROM restaurante WHERE zona = ? ORDER BY nombre",
                          MAPPER, zona);
    }

    public List<Restaurante> findByTipoComida(String tipoComida) {
        return jdbc.query("SELECT " + COLUMNAS
                        + " FROM restaurante WHERE tipo_comida = ? ORDER BY nombre",
                          MAPPER, tipoComida);
    }

    public List<Restaurante> findFavoritosDeUsuario(int idUsuario) {
    return jdbc.query("SELECT " + COLUMNAS + " FROM restaurante "
                    + "WHERE id_restaurante IN "
                    + "(SELECT id_restaurante FROM favorito WHERE id_usuario = ?) "
                    + "ORDER BY nombre",
                      MAPPER, idUsuario);
    }

    // ── CREATE ─────────────────────────────────────────────────────────

    /**
     * INSERT y devuelve el restaurante con el id generado.
     * promedio_general y total_calificaciones no se insertan: usan el DEFAULT 0 de la tabla.
     */
    public Restaurante save(Restaurante restaurante) {
        String sql = """
                INSERT INTO restaurante (id_dueno, nombre, tipo_comida, direccion, zona,
                                         telefono, hora_apertura, hora_cierre)
                VALUES (:idDueno, :nombre, :tipoComida, :direccion, :zona,
                        :telefono, :horaApertura, :horaCierre)
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("idDueno",      restaurante.getIdDueno())
                .addValue("nombre",       restaurante.getNombre())
                .addValue("tipoComida",   restaurante.getTipoComida())
                .addValue("direccion",    restaurante.getDireccion())
                .addValue("zona",         restaurante.getZona())
                .addValue("telefono",     restaurante.getTelefono())
                .addValue("horaApertura", restaurante.getHoraApertura())
                .addValue("horaCierre",   restaurante.getHoraCierre());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbc.update(sql, params, keyHolder, new String[]{"id_restaurante"});
        restaurante.setIdRestaurante(keyHolder.getKey().intValue());
        return restaurante;
    }

    // ── UPDATE ─────────────────────────────────────────────────────────

    /** true si actualizó una fila; false si el id no existe. */
    public boolean update(int id, Restaurante restaurante) {
        String sql = """
                UPDATE restaurante
                   SET id_dueno = :idDueno, nombre = :nombre, tipo_comida = :tipoComida,
                       direccion = :direccion, zona = :zona, telefono = :telefono,
                       hora_apertura = :horaApertura, hora_cierre = :horaCierre
                 WHERE id_restaurante = :id
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("idDueno",      restaurante.getIdDueno())
                .addValue("nombre",       restaurante.getNombre())
                .addValue("tipoComida",   restaurante.getTipoComida())
                .addValue("direccion",    restaurante.getDireccion())
                .addValue("zona",         restaurante.getZona())
                .addValue("telefono",     restaurante.getTelefono())
                .addValue("horaApertura", restaurante.getHoraApertura())
                .addValue("horaCierre",   restaurante.getHoraCierre())
                .addValue("id",           id);
        return namedJdbc.update(sql, params) > 0;
    }

    // ── DELETE ─────────────────────────────────────────────────────────

    /** true si borró una fila; false si el id no existe. */
    public boolean delete(int id) {
        return jdbc.update("DELETE FROM restaurante WHERE id_restaurante = ?", id) > 0;
    }


    public void registrarConMenu(Integer idDueno, String nombre, String tipoComida,
                             String direccion, String zona, String telefono,
                             String horaApertura, String horaCierre, String platosJson) {
    jdbc.update("""
            CALL pr_registrar_restaurante_con_menu(
                CAST(? AS INT), ?, ?, ?, ?, ?,
                CAST(? AS TIME), CAST(? AS TIME), CAST(? AS JSONB))
            """,
            idDueno, nombre, tipoComida, direccion, zona, telefono,
            horaApertura, horaCierre, platosJson);
    }
}
