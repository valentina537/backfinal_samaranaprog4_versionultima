package com.samarana.back_samarana.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "alojamiento")
@Schema(description = "Modelo que representa un bungalow o alojamiento disponible para reservar")
public class Alojamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del alojamiento", example = "1")
    private Integer id_alojamiento;
    @Schema(description = "Nombre comercial del bungalow", example = "Bungalow A")
    private String nombre_alojamiento;

    private String descripcion;
    @Schema(description = "Precio base por noche para el alojamiento", example = "45000.00")
    private Double precio_base;
    private Double precio_full;
    @Schema(description = "Capacidad máxima de huéspedes permitida", example = "5")
    private Integer capacidad_max;
}
