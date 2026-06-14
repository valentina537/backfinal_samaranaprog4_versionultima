package com.samarana.back_samarana.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "reserva")
@Schema(description = "Modelo que representa una reserva en el sistema")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID autoincremental de la reserva", example="1")
    private Long id_reserva;
    @Schema(description = "Fecha de ingreso al bungalow (YYYY-MM-DD)", example="2026-07-01")
    private LocalDate fecha_inicio;
    @Schema(description = "Fecha de egreso del bungalow (YYYY-MM-DD)", example="2026-07-05")
    private LocalDate fecha_fin;
    @Schema(description = "Cantidad de huéspedes", example="4")
    private Integer cantidad_personas;
    @Schema(description = "Tarifa total calculada para la estadía", example="150000.00")
    private Double total_tarifa;

    @ManyToOne
    @JoinColumn(name = "id_alojamiento")
    private Alojamiento alojamiento;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_admin")
    private Administrador administrador;

    @ManyToOne
    @JoinColumn(name = "id_estado")
    @Schema(description = "Estado de la reserva: 0=Pendiente, 1=Confirmada, 2=Cancelada", example = "0")
    private EstadoReserva estado;
}