package com.samarana.back_samarana;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samarana.back_samarana.entities.Alojamiento;
import com.samarana.back_samarana.entities.Reserva;
import com.samarana.back_samarana.repositories.AlojamientoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Evita que los datos de prueba queden guardados para siempre en tu BD
public class ReservaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AlojamientoRepository alojamientoRepository;


    // ==================== ESCENARIO 1: CAMINO FELIZ (Guardar Reserva) ======================
    @Test
    @WithMockUser(username = "juan_perez")
    public void guardarReserva_CaminoFeliz_DeberiaDevolverOk() throws Exception {
        Alojamiento alojamiento = new Alojamiento();
        alojamiento.setNombre_alojamiento("Bungalow Test");
        alojamiento.setCapacidad_max(4);
        alojamiento.setPrecio_base(30000.0);
        alojamiento = alojamientoRepository.save(alojamiento);

        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setFecha_inicio(LocalDate.now().plusDays(1));
        nuevaReserva.setFecha_fin(LocalDate.now().plusDays(5));
        nuevaReserva.setCantidad_personas(2);
        nuevaReserva.setAlojamiento(alojamiento);

        mockMvc.perform(post("/api/reservas/guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevaReserva)))
                .andExpect(status().isOk());
    }

// ==================== ESCENARIO 2: ESCENARIO DE ERROR (Fechas al revés) ======================
    @Test
    @WithMockUser(username = "juan_perez")
    public void guardarReserva_FechasInvalidadas_DeberiaDevolverBadRequest() throws Exception {
        Alojamiento alojamiento = new Alojamiento();
        alojamiento.setNombre_alojamiento("Bungalow Test Error");
        alojamiento.setCapacidad_max(4);
        alojamiento = alojamientoRepository.save(alojamiento);

        Reserva reservaInvalida = new Reserva();

        reservaInvalida.setFecha_inicio(LocalDate.now().plusDays(5));
        reservaInvalida.setFecha_fin(LocalDate.now().plusDays(1));
        reservaInvalida.setCantidad_personas(2);
        reservaInvalida.setAlojamiento(alojamiento);

        mockMvc.perform(post("/api/reservas/guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaInvalida)))
                .andExpect(status().isBadRequest());
    }
}