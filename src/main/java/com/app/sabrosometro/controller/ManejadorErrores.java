package com.app.sabrosometro.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.Map;

/**
 * Convierte los errores de PostgreSQL en respuestas HTTP claras,
 * en lugar del "500 Internal Server Error" genérico.
 */
@RestControllerAdvice
public class ManejadorErrores {

    /** Violó una restricción: FK, CHECK, UNIQUE o NOT NULL → 409 Conflict. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> integridad(
            DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", mensajeDePostgres(e)));
    }

    /**
     * RAISE EXCEPTION de un procedimiento (SQLSTATE P0001) → 400 Bad Request.
     * Cualquier otro error de base de datos → 500, y el detalle queda en la consola.
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, String>> baseDeDatos(DataAccessException e) {
        if (e.getMostSpecificCause() instanceof SQLException sql
                && "P0001".equals(sql.getSQLState())) {
            return ResponseEntity.badRequest().body(Map.of("error", mensajeDePostgres(e)));
        }
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error de base de datos: revisen la consola"));
    }

    /** Primera línea del mensaje de PostgreSQL, sin el prefijo "ERROR: ". */
    private static String mensajeDePostgres(DataAccessException e) {
        String m = e.getMostSpecificCause().getMessage();
        return m.split("\n")[0].replace("ERROR: ", "");
    }
}
