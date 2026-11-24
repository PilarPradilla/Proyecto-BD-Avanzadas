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

import com.app.sabrosometro.model.CalificacionPlato;
import com.app.sabrosometro.repository.CalificacionPlatoRepository;

/*
GET /calificacion-plato
GET /calificacion-plato/{id}
GET /calificacion-plato/plato/{idPlato}
POST /calificacion-plato +  JSON
PUT /calificacion-plato/{id}
DELETE /calificacion-plato/{id}
*/

@RestController
@RequestMapping("/calificacion-plato")
public class CalificacionPlatoController {
    private final CalificacionPlatoRepository repo;

    public CalificacionPlatoController(CalificacionPlatoRepository repo) { this.repo = repo; }

    @GetMapping
    public List<CalificacionPlato> listar() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<CalificacionPlato> obtener(@PathVariable int id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/plato/{idPlato}")
    public List<CalificacionPlato> porPlato(@PathVariable int idPlato) { return repo.findByPlato(idPlato); }

    @PostMapping
    public ResponseEntity<CalificacionPlato> crear(@RequestBody CalificacionPlato c) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.insert(c));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalificacionPlato> actualizar(@PathVariable int id, @RequestBody CalificacionPlato c) {
        if (repo.update(id, c) == 0) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(repo.findById(id).orElseThrow());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        return repo.delete(id) == 0 ? ResponseEntity.notFound().build() : ResponseEntity.noContent().build();
    }
}