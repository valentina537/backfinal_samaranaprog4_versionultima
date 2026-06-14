package com.samarana.back_samarana.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Data
@Table(name = "Usuario")
@Schema(description = "Modelo que representa a los usuarios del sistema")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del usuario", example="12")
    private Integer id_usuario;
    @Schema(description = "Nombre real del usuario", example="Juan")
    private String nombre;
    @Schema(description = "Apellido real del usuario", example="Pérez")
    private String apellido;
    @Schema(description = "Nombre de usuario para el inicio de sesión", example="juan_perez")
    private String usuario;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String contrasena;
    @Schema(description = "Documento Nacional de Identidad", example="38444555")
    private String dni;
    @Schema(description = "Correo electrónico del usuario", example="juan.perez@email.com")
    private String email;
    @Schema(description = "Teléfono del usuario contacto", example="3447123456")
    private String telefono;
}
