package com.samarana.back_samarana.services;

import com.samarana.back_samarana.entities.Administrador;
import com.samarana.back_samarana.repositories.AdministradorRepository;
import lombok.extern.slf4j.Slf4j; // <-- Import de Lombok
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    public boolean autenticar(String nombreUsuario, String passwordIngresada) {
        Optional<Administrador> adminOpt = administradorRepository.findByUsuario(nombreUsuario);

        if (adminOpt.isPresent()) {
            Administrador admin = adminOpt.get();
            boolean esValido = admin.getContrasena().equals(passwordIngresada);

            if (!esValido) {

                log.warn("Alerta de seguridad: Contraseña incorrecta para el administrador '{}'.", nombreUsuario);
            }
            return esValido;
        }


        log.warn("Alerta de seguridad: Intento de inicio de sesión fallido. No existe el administrador '{}'.", nombreUsuario);
        return false;
    }

    public Optional<Administrador> obtenerPorUsuario(String nombreUsuario) {
        return administradorRepository.findByUsuario(nombreUsuario);
    }

    public List<Administrador> obtenerTodos() {
        return administradorRepository.findAll();
    }

    public Administrador guardarAdministrador(Administrador administrador) {
        return administradorRepository.save(administrador);
    }
}