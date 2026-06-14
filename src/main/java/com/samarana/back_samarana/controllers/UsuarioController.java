package com.samarana.back_samarana.controllers;

import com.samarana.back_samarana.entities.Usuario;
import com.samarana.back_samarana.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/listar")
    public List<Usuario> listar() {
        return usuarioService.listarTodos();
    }

    @PostMapping("/guardar")
    public ResponseEntity<?> guardar(@RequestBody Usuario usuario) {
        try {
            usuarioService.guardar(usuario);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("mensaje", "Usuario registrado correctamente."));
        } catch (IllegalArgumentException e) {
            HttpStatus status = e.getMessage().contains("registrado")
                    ? HttpStatus.CONFLICT
                    : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(e.getMessage());
        }
    }

}
