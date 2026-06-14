package com.samarana.back_samarana.services;

import com.samarana.back_samarana.entities.Alojamiento;
import com.samarana.back_samarana.repositories.AlojamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlojamientoService {

    @Autowired
    private AlojamientoRepository alojamientoRepository;

    public List<Alojamiento> listarTodos() {
        return alojamientoRepository.findAll();
    }

    public Alojamiento guardar(Alojamiento alojamiento) {
        return alojamientoRepository.save(alojamiento);
    }

    public void eliminar(Integer id) {
        alojamientoRepository.deleteById(id);
    }
}
