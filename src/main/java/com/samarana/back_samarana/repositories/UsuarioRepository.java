package com.samarana.back_samarana.repositories;

import com.samarana.back_samarana.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByUsuarioAndContrasena(
            String usuario,
            String contrasena
    );

    Optional<Usuario> findByUsuario(String usuario);

    Optional<Usuario> findByEmailIgnoreCase(String email);
}
