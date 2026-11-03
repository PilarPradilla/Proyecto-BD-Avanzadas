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

import com.app.sabrosometro.model.Restaurante;
import com.app.sabrosometro.repository.RestauranteRepository;

/**
 * Endpoints REST de "restaurantes":
 *   GET    /restaurantes                    → lista todos            200
 *   GET    /restaurantes/{id}               → uno por id             200 · 404
 *   GET    /restaurantes/zona/{zona}        → los de una zona        200
 *   GET    /restaurantes/tipo/{tipo}        → los de un tipo comida  200
 *   POST   /restaurantes        + JSON      → crea                   201
 *   PUT    /restaurantes/{id}   + JSON      → actualiza              200 · 404
 *   DELETE /restaurantes/{id}               → elimina                204 · 404
 */
@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
    private final RestauranteRepository repo;

    public RestauranteController(RestauranteRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Restaurante> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurante> buscar(@PathVariable("id") int id) {
        return repo.findById(id)
                   .map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/zona/{zona}")
    public List<Restaurante> porZona(@PathVariable("zona") String zona) {
        return repo.findByZona(zona);
    }

    @GetMapping("/tipo/{tipo}")
    public List<Restaurante> porTipo(@PathVariable("tipo") String tipo) {
        return repo.findByTipoComida(tipo);
    }

    @PostMapping
    public ResponseEntity<Restaurante> crear(@RequestBody Restaurante restaurante) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(restaurante));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(@PathVariable("id") int id,
                                           @RequestBody Restaurante restaurante) {
        return repo.update(id, restaurante)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") int id) {
        return repo.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/api/favoritos/usuario/{id}")
    public List<Restaurante> favoritos(@PathVariable("id") int id) {
        return repo.findFavoritosDeUsuario(id);
    }
}
