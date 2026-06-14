package com.samarana.back_samarana.repositories;

import com.samarana.back_samarana.entities.Alojamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlojamientoRepository extends JpaRepository<Alojamiento, Integer> {
}
