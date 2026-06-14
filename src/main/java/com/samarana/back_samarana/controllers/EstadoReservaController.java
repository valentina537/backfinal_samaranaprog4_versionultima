package com.samarana.back_samarana.controllers;

import com.samarana.back_samarana.entities.EstadoReserva;
import com.samarana.back_samarana.repositories.EstadoReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/estados")
@CrossOrigin(origins = "*")
public class EstadoReservaController {

    @Autowired
    private EstadoReservaRepository estadoRepository;

    @GetMapping("/listar")
    public List<EstadoReserva> listar() {
        return estadoRepository.findAll();
    }
}