package com.app.sabrosometro.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.sabrosometro.model.CalificacionRestaurante;
import com.app.sabrosometro.repository.CalificacionRestauranteRepository;

/*
GET /calificacion-restaurante
GET /calificacion-restaurante/{id}
GET /calificacion-restaurante/restaurante/{idRestaurante}
POST /calificacion-restaurante +  JSON
PUT /calificacion-restaurante/{id}
DELETE /calificacion-restaurante/{id}
*/

@RestController
@RequestMapping("/calificacion-restaurante")
public class CalificacionRestauranteController {
    private final CalificacionRestauranteRepository repo;

    public CalificacionRestauranteController(CalificacionRestauranteRepository repo) { this.repo = repo; }

    @GetMapping
    public List<CalificacionRestaurante> listar() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<CalificacionRestaurante> obtener(@PathVariable int id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/restaurante/{idRestaurante}")
    public List<CalificacionRestaurante> porRestaurante(@PathVariable int idRestaurante) {
        return repo.findByRestaurante(idRestaurante);
    }

    @PostMapping
    public ResponseEntity<CalificacionRestaurante> crear(@RequestBody CalificacionRestaurante c) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.insert(c));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalificacionRestaurante> actualizar(@PathVariable int id, @RequestBody CalificacionRestaurante c) {
        if (repo.update(id, c) == 0) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(repo.findById(id).orElseThrow());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        return repo.delete(id) == 0 ? ResponseEntity.notFound().build() : ResponseEntity.noContent().build();
    }
}