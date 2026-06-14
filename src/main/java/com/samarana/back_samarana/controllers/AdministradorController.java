package com.samarana.back_samarana.controllers;

// Estos imports son fundamentales para que no marque error rojo
import com.samarana.back_samarana.dto.LoginRequest;
import com.samarana.back_samarana.entities.Administrador;
import com.samarana.back_samarana.services.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/administradores")
@CrossOrigin(origins = "*")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    /**
     * Endpoint para el login de administradores.
     * Recibe un JSON con 'usuario' y 'contrasena'.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        boolean isAuthenticated =
                administradorService.autenticar(
                        loginRequest.usuario(),
                        loginRequest.contrasena()
                );
        if (isAuthenticated) {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Bienvenido al panel de control",
                    "usuario", loginRequest.usuario()
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of(
                    "status", "error",
                    "message", "Credenciales inválidas"
            ));
        }
    }

    /**
     * Endpoint para listar todos los administradores.
     */
    @GetMapping
    public List<Administrador> listarTodos() {
        return administradorService.obtenerTodos();
    }

    /**
     * Endpoint para crear un nuevo administrador.
     */
    @PostMapping("/crear")
    public Administrador guardar(@RequestBody Administrador administrador) {
        return administradorService.guardarAdministrador(administrador);
    }
}