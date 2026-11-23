package com.app.sabrosometro.controller;

import com.app.sabrosometro.repository.RestauranteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// Spring Boot 4 usa Jackson 3:
import tools.jackson.databind.ObjectMapper;
// Si tu IDE no lo encuentra, usa: com.fasterxml.jackson.databind.ObjectMapper

import java.util.List;
import java.util.Map;

/** POST /restaurantes-con-menu → 201 si funciona · 400 con el mensaje del procedimiento. */
@RestController
@RequestMapping("/restaurantes-con-menu")
public class RestauranteMenuController {

    record PlatoInicial(String nombre, String descripcion, java.math.BigDecimal precio) { }

    record RestauranteConMenu(Integer idDueno, String nombre, String tipoComida,
                              String direccion, String zona, String telefono,
                              String horaApertura, String horaCierre,
                              List<PlatoInicial> platos) { }

    private final RestauranteRepository repo;
    private final ObjectMapper mapper;

    public RestauranteMenuController(RestauranteRepository repo, ObjectMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> crear(@RequestBody RestauranteConMenu r) throws Exception {
        String platosJson = mapper.writeValueAsString(r.platos());
        repo.registrarConMenu(r.idDueno(), r.nombre(), r.tipoComida(), r.direccion(),
                r.zona(), r.telefono(), r.horaApertura(), r.horaCierre(), platosJson);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Restaurante registrado con su menú"));
    }
}