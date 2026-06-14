package com.samarana.back_samarana.controllers;

import com.samarana.back_samarana.entities.Alojamiento;
import com.samarana.back_samarana.services.AlojamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alojamientos")
@CrossOrigin(origins = "*")
public class AlojamientoController {

    @Autowired
    private AlojamientoService alojamientoService;

    @GetMapping("/listar")
    public List<Alojamiento> listar() {
        return alojamientoService.listarTodos();
    }

    @PostMapping("/guardar")
    public Alojamiento guardar(@RequestBody Alojamiento alojamiento) {
        return alojamientoService.guardar(alojamiento);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Integer id) {
        alojamientoService.eliminar(id);
    }
}
