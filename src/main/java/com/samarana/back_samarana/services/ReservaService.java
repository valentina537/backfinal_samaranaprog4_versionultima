package com.samarana.back_samarana.services;

import com.samarana.back_samarana.entities.Alojamiento;
import com.samarana.back_samarana.entities.EstadoReserva;
import com.samarana.back_samarana.entities.Reserva;
import com.samarana.back_samarana.entities.Usuario;
import com.samarana.back_samarana.repositories.AlojamientoRepository;
import com.samarana.back_samarana.repositories.EstadoReservaRepository;
import com.samarana.back_samarana.repositories.ReservaRepository;
import com.samarana.back_samarana.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReservaService {

    private static final int ESTADO_CANCELADO = 3;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EstadoReservaRepository estadoReservaRepository;

    @Autowired
    private AlojamientoRepository alojamientoRepository;

    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    public Reserva guardar(Reserva nuevaReserva, String nombreUsuario) throws Exception {

        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            log.warn("Intento de reserva rechazado: Nombre de usuario vacío o nulo.");
            throw new Exception("Debe iniciar sesion para reservar.");
        }

        Usuario usuario = usuarioRepository.findByUsuario(nombreUsuario)
                .orElseThrow(() -> {
                    log.warn("Intento de reserva rechazado: No existe el usuario '{}' en la base de datos.", nombreUsuario);
                    return new Exception("Debe iniciar sesion como usuario para reservar.");
                });

        if (nuevaReserva.getFecha_inicio() == null || nuevaReserva.getFecha_fin() == null) {
            log.warn("Intento de reserva rechazado para usuario '{}': Fechas incompletas (Inicio: {}, Fin: {}).",
                    nombreUsuario, nuevaReserva.getFecha_inicio(), nuevaReserva.getFecha_fin());
            throw new Exception("Debe seleccionar fecha de ingreso y egreso.");
        }

        if (nuevaReserva.getAlojamiento() == null || nuevaReserva.getAlojamiento().getId_alojamiento() == null) {
            log.warn("Intento de reserva rechazado para usuario '{}': No se especificó ID de alojamiento.", nombreUsuario);
            throw new Exception("Debe especificar un alojamiento valido.");
        }

        Alojamiento alojamiento = alojamientoRepository.findById(nuevaReserva.getAlojamiento().getId_alojamiento())
                .orElseThrow(() -> {
                    log.warn("Intento de reserva rechazado: El alojamiento ID {} no existe.", nuevaReserva.getAlojamiento().getId_alojamiento());
                    return new Exception("El alojamiento especificado no existe.");
                });

        List<Reserva> reservasExistentes = reservaRepository.findAll().stream()
                .filter(r -> r.getAlojamiento() != null)
                .filter(r -> r.getAlojamiento().getId_alojamiento().equals(alojamiento.getId_alojamiento()))
                .filter(r -> r.getEstado() == null || r.getEstado().getId_estado() == null
                        || !r.getEstado().getId_estado().equals(ESTADO_CANCELADO))
                .collect(Collectors.toList());

        for (Reserva existente : reservasExistentes) {
            if (nuevaReserva.getFecha_inicio().isBefore(existente.getFecha_fin()) &&
                    existente.getFecha_inicio().isBefore(nuevaReserva.getFecha_fin())) {

                log.warn("Conflicto de reserva (Bungalow ocupado): El alojamiento ID {} ya está reservado entre {} y {}. Intento fallido para usuario: '{}' (Fechas solicitadas: {} a {})",
                        alojamiento.getId_alojamiento(), existente.getFecha_inicio(), existente.getFecha_fin(),
                        nombreUsuario, nuevaReserva.getFecha_inicio(), nuevaReserva.getFecha_fin());
                throw new Exception("Lo sentimos, el bungalow ya está ocupado en esas fechas.");
            }
        }

        long noches = ChronoUnit.DAYS.between(nuevaReserva.getFecha_inicio(), nuevaReserva.getFecha_fin());
        if (noches <= 0) {
            log.warn("Intento de reserva rechazado: Rango de fechas inválido. Entrada: {}, Salida: {} (Noches calculadas: {})",
                    nuevaReserva.getFecha_inicio(), nuevaReserva.getFecha_fin(), noches);
            throw new Exception("La fecha de salida debe ser posterior a la de entrada.");
        }

        EstadoReserva estadoInicial = estadoReservaRepository.findById(1)
                .orElseThrow(() -> {
                    log.warn("Error interno de configuración: No se encontró el estado Pendiente (ID 1) en la tabla estado_reserva.");
                    return new Exception("Error interno: no se encontró el estado Pendiente.");
                });

        int cantidadPersonas = nuevaReserva.getCantidad_personas() == null
                ? 1
                : nuevaReserva.getCantidad_personas();
        if (cantidadPersonas < 1) {
            cantidadPersonas = 1;
        }
        if (alojamiento.getCapacidad_max() != null && cantidadPersonas > alojamiento.getCapacidad_max()) {

            log.warn("Intento de reserva rechazado: Cantidad de personas ({}) supera la capacidad máxima ({}) del alojamiento ID: {}",
                    cantidadPersonas, alojamiento.getCapacidad_max(), alojamiento.getId_alojamiento());
            throw new Exception("La cantidad de personas supera la capacidad del bungalow.");
        }

        double precioBase = alojamiento.getPrecio_base() == null ? 0 : alojamiento.getPrecio_base();
        double precioFull = alojamiento.getPrecio_full() == null ? precioBase : alojamiento.getPrecio_full();
        double precioPorNoche = cantidadPersonas > 2 ? precioFull : precioBase;

        nuevaReserva.setAlojamiento(alojamiento);
        nuevaReserva.setUsuario(usuario);
        nuevaReserva.setCantidad_personas(cantidadPersonas);
        nuevaReserva.setEstado(estadoInicial);
        nuevaReserva.setTotal_tarifa(precioPorNoche * noches);

        return reservaRepository.save(nuevaReserva);
    }

    public Reserva cambiarEstado(Long idReserva, Integer idNuevoEstado) throws Exception {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> {
                    log.warn("Cambio de estado fallido: No se encontró la reserva con ID: {}", idReserva);
                    return new Exception("No se encontró la reserva con ID: " + idReserva);
                });

        EstadoReserva nuevoEstado = estadoReservaRepository.findById(idNuevoEstado)
                .orElseThrow(() -> {
                    log.warn("Cambio de estado fallido: El estado ID {} solicitado no existe.", idNuevoEstado);
                    return new Exception("El estado solicitado no existe.");
                });

        reserva.setEstado(nuevoEstado);
        return reservaRepository.save(reserva);
    }
}