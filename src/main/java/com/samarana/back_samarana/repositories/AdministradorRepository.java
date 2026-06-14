package com.samarana.back_samarana.repositories;

import com.samarana.back_samarana.entities.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Este import es clave para que no te dé error en el método findByUsuario
import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {

    /**
     * Busca un administrador por su nombre de usuario.
     * Devolvemos un Optional para manejar de forma segura si el usuario existe o no.
     */
    Optional<Administrador> findByUsuarioAndContrasena(
            String usuario,
            String contrasena
    );

    Optional<Administrador> findByUsuario(String usuario);

}
