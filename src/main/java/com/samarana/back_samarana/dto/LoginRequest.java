package com.samarana.back_samarana.dto;
import lombok.Data;


public record LoginRequest(String usuario, String contrasena) {
}
