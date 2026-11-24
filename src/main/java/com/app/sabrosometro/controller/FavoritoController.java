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

import com.app.sabrosometro.model.Favorito;
import com.app.sabrosometro.repository.FavoritoRepository;

/*
GET /favorito
GET /favorito/{id}
GET /favorito/{idUsuario}
POST /favorito +  JSON
PUT /favorito/{id}
DELETE /favorito/{id}
*/

@RestController
@RequestMapping("/favorito")
public class FavoritoController {
    private final FavoritoRepository repo;

    public FavoritoController(FavoritoRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Favorito> listar() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Favorito> obtener(@PathVariable int id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<Favorito> porUsuario(@PathVariable int idUsuario) { return repo.findByUsuario(idUsuario); }

    @PostMapping
    public ResponseEntity<Favorito> crear(@RequestBody Favorito f) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.insert(f));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Favorito> actualizar(@PathVariable int id, @RequestBody Favorito f) {
        if (repo.update(id, f) == 0) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(repo.findById(id).orElseThrow());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        return repo.delete(id) == 0 ? ResponseEntity.notFound().build() : ResponseEntity.noContent().build();
    }
}
