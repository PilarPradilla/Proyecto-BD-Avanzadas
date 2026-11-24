package com.app.sabrosometro.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.sabrosometro.model.HistorialPrecioPlato;
import com.app.sabrosometro.repository.HistorialPrecioPlatoRepository;

/*
GET /historial-precio
GET /historial-precio/{id}
GET /historial-precio/{idPlato}
*/

@RestController
@RequestMapping("/historial-precio")
public class HistorialPrecioPlatoController {
    private final HistorialPrecioPlatoRepository repo;

    public HistorialPrecioPlatoController(HistorialPrecioPlatoRepository repo) { this.repo = repo; }

    @GetMapping
    public List<HistorialPrecioPlato> listar() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialPrecioPlato> obtener(@PathVariable int id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/plato/{idPlato}")
    public List<HistorialPrecioPlato> porPlato(@PathVariable int idPlato) {
        return repo.findByPlato(idPlato);
    }
}