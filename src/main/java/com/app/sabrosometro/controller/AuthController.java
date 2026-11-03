package com.app.sabrosometro.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.sabrosometro.model.Usuario;
import com.app.sabrosometro.repository.UsuarioRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {
    record RegistroRequest(String nombre, String correo, String contrasena) { }
    record LoginRequest(String correo, String contrasena) { }

    private final UsuarioRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(UsuarioRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegistroRequest req) {
        if (repo.findByCorreo(req.correo()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "El correo ya está registrado"));
        }
        Usuario u = new Usuario();
        u.setNombre(req.nombre());
        u.setCorreo(req.correo());
        u.setContrasenaHash(encoder.encode(req.contrasena()));
        u.setRol("cliente");      // el rol NO lo decide el cliente
        u.setEstado("activo");
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(u));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        var usuario = repo.findByCorreo(req.correo());
        if (usuario.isEmpty()
                || !encoder.matches(req.contrasena(), usuario.get().getContrasenaHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Correo o contraseña incorrectos"));
        }
        if (!"activo".equals(usuario.get().getEstado())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Tu cuenta está suspendida"));
        }
        return ResponseEntity.ok(usuario.get());
    }
}
