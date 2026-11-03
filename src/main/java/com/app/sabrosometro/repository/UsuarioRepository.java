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

import com.app.sabrosometro.model.Usuario;

@Repository 
public class UsuarioRepository {
    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public UsuarioRepository(JdbcTemplate jdbc, NamedParameterJdbcTemplate namedJdbc) {
        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    private static final String COLUMNAS =
            "id_usuario, nombre, correo, contrasena_hash, rol, estado, fecha_registro";

    private static final RowMapper<Usuario> MAPPER = (rs, rowNum) -> {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setNombre(rs.getString("nombre"));
        u.setCorreo(rs.getString("correo"));
        u.setContrasenaHash(rs.getString("contrasena_hash"));
        u.setRol(rs.getString("rol"));
        u.setEstado(rs.getString("estado"));
        u.setFechaRegistro(rs.getObject("fecha_registro", LocalDate.class));
        return u;
    };

    // ── READ ───────────────────────────────────────────────────────────

    public List<Usuario> findAll() {
        return jdbc.query("SELECT " + COLUMNAS + " FROM usuario ORDER BY id_usuario", MAPPER);
    }

    public Optional<Usuario> findById(int id) {
        return jdbc.query("SELECT " + COLUMNAS + " FROM usuario WHERE id_usuario = ?", MAPPER, id)
                   .stream()
                   .findFirst();
    }

    public Optional<Usuario> findByCorreo(String correo) {
        return jdbc.query("SELECT " + COLUMNAS + " FROM usuario WHERE correo = ?", MAPPER, correo)
                   .stream()
                   .findFirst();
    }

    public List<Usuario> findByRol(String rol) {
        return jdbc.query("SELECT " + COLUMNAS + " FROM usuario WHERE rol = ? ORDER BY nombre",
                          MAPPER, rol);
    }

    // ── CREATE ─────────────────────────────────────────────────────────

    /**
     * INSERT y devuelve el usuario completo (con id, rol, estado y fecha ya asignados).
     * COALESCE: si rol/estado vienen null se usa el DEFAULT de la tabla.
     */
    public Usuario save(Usuario usuario) {
        String sql = """
                INSERT INTO usuario (nombre, correo, contrasena_hash, rol, estado)
                VALUES (:nombre, :correo, :contrasenaHash,
                        COALESCE(:rol, 'cliente'), COALESCE(:estado, 'activo'))
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("nombre",         usuario.getNombre())
                .addValue("correo",         usuario.getCorreo())
                .addValue("contrasenaHash", usuario.getContrasenaHash())
                .addValue("rol",            usuario.getRol())
                .addValue("estado",         usuario.getEstado());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbc.update(sql, params, keyHolder, new String[]{"id_usuario"});
        int id = keyHolder.getKey().intValue();
        return findById(id).orElseThrow();
    }

    // ── UPDATE ─────────────────────────────────────────────────────────

    /** true si actualizó una fila; false si el id no existe. No toca la contraseña. */
    public boolean update(int id, Usuario usuario) {
        String sql = """
                UPDATE usuario
                   SET nombre = :nombre, correo = :correo, rol = :rol, estado = :estado
                 WHERE id_usuario = :id
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("nombre", usuario.getNombre())
                .addValue("correo", usuario.getCorreo())
                .addValue("rol",    usuario.getRol())
                .addValue("estado", usuario.getEstado())
                .addValue("id",     id);
        return namedJdbc.update(sql, params) > 0;
    }

    // ── DELETE ─────────────────────────────────────────────────────────

    public boolean delete(int id) {
        return jdbc.update("DELETE FROM usuario WHERE id_usuario = ?", id) > 0;
    }

}
