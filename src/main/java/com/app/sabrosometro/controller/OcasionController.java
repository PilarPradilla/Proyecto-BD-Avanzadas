package com.app.sabrosometro.controller;

import java.util.List;

import org.springframework.dao.DuplicateKeyException;
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

import com.app.sabrosometro.model.Ocasion;
import com.app.sabrosometro.repository.OcasionRepository;

/**
 * Endpoints REST de "ocasion":
 *   GET    /ocasion 
 *   GET    /ocasion/{id} 
 *   POST   /ocasion + JSON 
 *   PUT    /ocasion/{id} + JSON 
 *   DELETE /ocasion/{id} 
 */

@RestController
@RequestMapping("/ocasion")
public class OcasionController {
    private final OcasionRepository repo;

    public OcasionController(OcasionRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Ocasion> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ocasion> obtener(@PathVariable int id) {
        return repo.findById(id)
                   .map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ocasion> crear(@RequestBody Ocasion o) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(repo.insert(o));
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ocasion> actualizar(@PathVariable int id, @RequestBody Ocasion o) {
        try {
            if (repo.update(id, o) == 0) return ResponseEntity.notFound().build();
            o.setIdOcasion(id);
            return ResponseEntity.ok(o);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        return repo.delete(id) == 0
                ? ResponseEntity.notFound().build()
                : ResponseEntity.noContent().build();
    }
}