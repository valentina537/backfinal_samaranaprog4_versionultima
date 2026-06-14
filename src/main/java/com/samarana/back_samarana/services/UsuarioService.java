package com.samarana.back_samarana.services;

import com.samarana.back_samarana.entities.Usuario;
import com.samarana.back_samarana.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j; // <-- Agregamos el import de Lombok
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Slf4j
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> obtenerPorUsuario(String nombreUsuario) {
        return usuarioRepository.findByUsuario(nombreUsuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario registrar(Usuario usuario) {
        validarCamposObligatorios(usuario);
        normalizar(usuario);

        if (usuarioRepository.findByEmailIgnoreCase(usuario.getEmail()).isPresent()) {

            log.warn("Registro rechazado: El email '{}' ya está asociado a otra cuenta.", usuario.getEmail());
            throw new IllegalArgumentException("El email ya se encuentra registrado.");
        }

        if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) {

            log.warn("Registro rechazado: El nombre de usuario '{}' ya existe.", usuario.getUsuario());
            throw new IllegalArgumentException("El usuario ya se encuentra registrado.");
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario guardar(Usuario usuario) {
        return registrar(usuario);
    }

    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }

    private void validarCamposObligatorios(Usuario usuario) {
        if (usuario == null) {
            log.warn("Validación fallida: Se recibió un objeto de usuario nulo.");
            throw new IllegalArgumentException("Debe completar los datos del usuario.");
        }

        validarCampo(usuario.getNombre(), "nombre");
        validarCampo(usuario.getApellido(), "apellido");
        validarCampo(usuario.getDni(), "dni");
        validarCampo(usuario.getUsuario(), "usuario");
        validarCampo(usuario.getEmail(), "email");
        validarCampo(usuario.getTelefono(), "telefono");
        validarCampo(usuario.getContrasena(), "contrasena");
    }

    private void validarCampo(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {

            log.warn("Validación de usuario fallida: El campo obligatorio '{}' llegó vacío o nulo.", campo);
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
    }

    private void normalizar(Usuario usuario) {
        usuario.setNombre(limpiar(usuario.getNombre()));
        usuario.setApellido(limpiar(usuario.getApellido()));
        usuario.setDni(limpiar(usuario.getDni()));
        usuario.setUsuario(limpiar(usuario.getUsuario()));
        usuario.setEmail(limpiar(usuario.getEmail()).toLowerCase(Locale.ROOT));
        usuario.setTelefono(limpiar(usuario.getTelefono()));
        usuario.setContrasena(limpiar(usuario.getContrasena()));
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}