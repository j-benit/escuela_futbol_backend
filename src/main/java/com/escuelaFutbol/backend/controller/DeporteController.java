package com.escuelaFutbol.backend.controller;

import com.escuelaFutbol.backend.entity.Deporte;
import com.escuelaFutbol.backend.service.DeporteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deportes")
public class DeporteController {

    private final DeporteService deporteService;

    public DeporteController(DeporteService deporteService) {
        this.deporteService = deporteService;
    }

    @GetMapping
    public ResponseEntity<List<Deporte>> getAllDeportes() {
        return ResponseEntity.ok(deporteService.findAll());
    }

    @PostMapping
    public ResponseEntity<Deporte> createDeporte(@RequestBody Deporte deporte) {
        Deporte saved = deporteService.save(deporte);
        return ResponseEntity.ok(saved);
    }
}
