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

import com.app.sabrosometro.model.Usuario;
import com.app.sabrosometro.repository.UsuarioRepository;

/**
 * Endpoints REST de "usuarios":
 *   GET    /usuarios                → lista todos         200
 *   GET    /usuarios/{id}           → uno por id          200 · 404
 *   GET    /usuarios/rol/{rol}      → los de un rol       200
 *   POST   /usuarios    + JSON      → crea                201
 *   PUT    /usuarios/{id} + JSON    → actualiza           200 · 404
 *   DELETE /usuarios/{id}           → elimina             204 · 404
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioRepository repo;

    public UsuarioController(UsuarioRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Usuario> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable("id") int id) {
        return repo.findById(id)
                   .map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/rol/{rol}")
    public List<Usuario> porRol(@PathVariable("rol") String rol) {
        return repo.findByRol(rol);
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(@PathVariable("id") int id,
                                           @RequestBody Usuario usuario) {
        return repo.update(id, usuario)
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
