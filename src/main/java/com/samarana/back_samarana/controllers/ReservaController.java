package com.samarana.back_samarana.controllers;

import com.samarana.back_samarana.entities.Reserva;
import com.samarana.back_samarana.services.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@Tag(name = "Reservas", description = "Controlador para la gestión de reservas de bungalows")
@Slf4j
@CrossOrigin(origins = "*")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Operation(
            summary = "Listar todas las reservas",
            description = "Obtiene una lista con todas las reservas registradas en el sistema (Uso exclusivo de administración)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida con éxito.")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<Reserva>> listar() {
        // Log INFO sin parámetros (solo avisa la acción)
        log.info("Petición GET /api/reservas/listar - Solicitando listado completo de reservas.");

        return ResponseEntity.ok(reservaService.listarTodas());
    }

    @Operation(
            summary = "Crear o guardar una nueva reserva",
            description = "Registra una reserva en el sistema vinculándola al usuario autenticado a través del token de sesión."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva creada exitosamente y lista para WhatsApp."),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (fechas incorrectas o formato erróneo)."),
            @ApiResponse(responseCode = "401", description = "No autorizado. No se encontró una sesión activa."),
            @ApiResponse(responseCode = "404", description = "El bungalow seleccionado no existe en la base de datos."),
            @ApiResponse(responseCode = "409", description = "El bungalow ya se encuentra ocupado en las fechas seleccionadas.")
    })
    @PostMapping("/guardar")
    public ResponseEntity<?> guardar(@RequestBody Reserva reserva, Authentication authentication) {
        // Log INFO registrando los parámetros de entrada clave que vienen en la petición
        log.info("Petición POST /api/reservas/guardar - Parámetros recibidos: Cant. Personas: {}, Inicio: {}, Fin: {}, Usuario Autenticado: {}",
                reserva.getCantidad_personas(),
                reserva.getFecha_inicio(),
                reserva.getFecha_fin(),
                (authentication != null) ? authentication.getName() : "Anónimo");

        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Debes iniciar sesión para poder reservar.");
        }

        try {
            Reserva reservaGuardada = reservaService.guardar(reserva, authentication.getName());
            return ResponseEntity.ok(reservaGuardada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Cambiar el estado de una reserva",
            description = "Modifica el estado actual de una reserva (ej: 0=Pendiente, 1=Confirmada, 2=Cancelada) mediante su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado de la reserva actualizado con éxito."),
            @ApiResponse(responseCode = "404", description = "No se encontró ninguna reserva con el ID proporcionado.")
    })
    @PutMapping("/cambiar-estado/{id}")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestParam Integer nuevoEstado) {
        // Log INFO registrando las variables de ruta y de parámetro de entrada
        log.info("Petición PUT /api/reservas/cambiar-estado/{} - Parámetros recibidos: nuevoEstado={}", id, nuevoEstado);

        try {
            Reserva reservaActualizada = reservaService.cambiarEstado(id, nuevoEstado);
            return ResponseEntity.ok(reservaActualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}