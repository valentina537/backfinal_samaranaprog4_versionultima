package com.samarana.back_samarana.controllers;

import com.samarana.back_samarana.dto.LoginRequest;
import com.samarana.back_samarana.dto.LoginResponse;
import com.samarana.back_samarana.entities.Administrador;
import com.samarana.back_samarana.entities.Usuario;
import com.samarana.back_samarana.repositories.AdministradorRepository;
import com.samarana.back_samarana.repositories.UsuarioRepository;
import com.samarana.back_samarana.security.JWTUtil;
import com.samarana.back_samarana.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AdministradorRepository administradorRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final JWTUtil jwtUtil;

    public AuthController(AdministradorRepository administradorRepository,
                          UsuarioRepository usuarioRepository,
                          UsuarioService usuarioService,
                          JWTUtil jwtUtil) {
        this.administradorRepository = administradorRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request == null || estaVacio(request.usuario()) || estaVacio(request.contrasena())) {
            return ResponseEntity.badRequest().body("Debe completar usuario y contrasena.");
        }

        String usuarioLogin = request.usuario().trim();
        String contrasenaLogin = request.contrasena().trim();

        var adminOpt = administradorRepository
                .findByUsuarioAndContrasena(usuarioLogin, contrasenaLogin);

        if (adminOpt.isPresent()) {
            Administrador admin = adminOpt.get();
            String token = jwtUtil.generarToken(admin.getUsuario());
            return ResponseEntity.ok(
                    new LoginResponse(token, "ADMIN", admin.getUsuario())
            );
        }

        var usuarioOpt = usuarioRepository
                .findByUsuarioAndContrasena(usuarioLogin, contrasenaLogin);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String token = jwtUtil.generarToken(usuario.getUsuario());
            return ResponseEntity.ok(
                    new LoginResponse(token, "USER", usuario.getUsuario())
            );
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Usuario o contrasena incorrectos.");
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            usuarioService.registrar(usuario);
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

    private boolean estaVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
