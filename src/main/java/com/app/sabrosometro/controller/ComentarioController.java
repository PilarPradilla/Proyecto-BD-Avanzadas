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

import com.app.sabrosometro.model.Comentario;
import com.app.sabrosometro.repository.ComentarioRepository;

/*
GET /comentario
GET /comentario/{id}
GET /comentario/{idRestaurante}
POST /comentario +  JSON
PUT /comentario/{id}
DELETE /comentario/{id}
*/

@RestController
@RequestMapping("/comentario")
public class ComentarioController {
    private final ComentarioRepository repo;

    public ComentarioController(ComentarioRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Comentario> listar() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Comentario> obtener(@PathVariable int id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/restaurante/{idRestaurante}")
    public List<Comentario> porRestaurante(@PathVariable int idRestaurante) {
        return repo.findByRestaurante(idRestaurante);
    }

    @PostMapping
    public ResponseEntity<Comentario> crear(@RequestBody Comentario c) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.insert(c));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comentario> actualizar(@PathVariable int id, @RequestBody Comentario c) {
        if (repo.update(id, c) == 0) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(repo.findById(id).orElseThrow());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        return repo.delete(id) == 0 ? ResponseEntity.notFound().build() : ResponseEntity.noContent().build();
    }
}