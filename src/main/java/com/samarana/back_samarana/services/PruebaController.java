package com.samarana.back_samarana.services;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping ("/api/prueba")

public class PruebaController {
    @GetMapping
    public String probar(){
        return "Backend Funcionando";
    }

}