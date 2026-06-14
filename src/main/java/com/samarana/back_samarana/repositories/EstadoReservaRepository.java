package com.samarana.back_samarana.repositories;

import com.samarana.back_samarana.entities.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoReservaRepository extends JpaRepository<EstadoReserva, Integer> {
}
