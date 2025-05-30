package com.escuelaFutbol.backend.controller;

import com.escuelaFutbol.backend.entity.Jugador;
import com.escuelaFutbol.backend.service.JugadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jugadores")
public class JugadorController {

    private final JugadorService jugadorService;

    public JugadorController(JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    @GetMapping
    public ResponseEntity<List<Jugador>> getAllJugadores() {
        return ResponseEntity.ok(jugadorService.findAll());
    }

    @PostMapping
    public ResponseEntity<Jugador> createJugador(@RequestBody Jugador jugador) {
        Jugador saved = jugadorService.save(jugador);
        return ResponseEntity.ok(saved);
    }
}
