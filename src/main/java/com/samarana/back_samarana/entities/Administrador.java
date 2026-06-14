package com.samarana.back_samarana.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
        import lombok.Data;

@Entity
@Data
@Table(name = "Administrador")
@Schema(description = "Modelo que representa a los administradores del sistema")
public class Administrador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_admin")
    @Schema(description = "Número identificador del administrador", example="1")
    private Integer id_admin;

    @Column(nullable = false, unique = true)
    @Schema(description = "Nombre de usuario para el inicio de sesión", example="admin")
    private String usuario;

    @Column(nullable = false)
    private String contrasena;
    @Schema(description = "Correo electrónico del administrador", example="admin@samarana.com")
    private String email;
}
