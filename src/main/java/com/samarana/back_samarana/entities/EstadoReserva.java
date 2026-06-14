package com.samarana.back_samarana.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Estado_Reserva")
public class EstadoReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_estado;

    private String nombre_estado;
}
