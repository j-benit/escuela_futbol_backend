package com.escuelaFutbol.backend.service;

import com.escuelaFutbol.backend.entity.Jugador;
import com.escuelaFutbol.backend.repository.JugadorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JugadorService {

    private final JugadorRepository jugadorRepository;

    public JugadorService(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    public List<Jugador> findAll() {
        return jugadorRepository.findAll();
    }

    public Jugador save(Jugador jugador) {
        return jugadorRepository.save(jugador);
    }
}
