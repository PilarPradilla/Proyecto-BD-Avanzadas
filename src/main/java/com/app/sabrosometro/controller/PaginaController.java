package com.app.sabrosometro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PaginaController {

    @GetMapping("/inicio")
    public String inicio() { return "inicio"; }

    @GetMapping("/restaurantes-pagina")
    public String restaurantes() { return "restaurantes"; }

    @GetMapping("/favoritos")
    public String favoritos() { return "favoritos"; }

    @GetMapping("/perfil/{id}")
    public String perfil(@PathVariable("id") int id) { return "perfil"; }

    @GetMapping("/restaurante/{id}")
    public String detalleRestaurante(@PathVariable("id") int id) { return "restaurante"; }
}