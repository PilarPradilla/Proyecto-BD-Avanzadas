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

import com.app.sabrosometro.model.Plato;
import com.app.sabrosometro.repository.PlatoRepository;

/**
 * Endpoints REST de "platos":
 *   GET    /platos                          → lista todos              200
 *   GET    /platos/{id}                     → uno por id               200 · 404
 *   GET    /platos/restaurante/{id}         → los de un restaurante    200
 *   POST   /platos          + JSON          → crea                     201
 *   PUT    /platos/{id}     + JSON          → actualiza                200 · 404
 *   DELETE /platos/{id}                     → elimina                  204 · 404
 */
@RestController
@RequestMapping("/platos")
public class PlatoController {
    private final PlatoRepository repo;

    public PlatoController(PlatoRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Plato> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plato> buscar(@PathVariable("id") int id) {
        return repo.findById(id)
                   .map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/restaurante/{idRestaurante}")
    public List<Plato> porRestaurante(@PathVariable("idRestaurante") int idRestaurante) {
        return repo.findByRestaurante(idRestaurante);
    }

    @PostMapping
    public ResponseEntity<Plato> crear(@RequestBody Plato plato) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(plato));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(@PathVariable("id") int id,
                                           @RequestBody Plato plato) {
        return repo.update(id, plato)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") int id) {
        return repo.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
