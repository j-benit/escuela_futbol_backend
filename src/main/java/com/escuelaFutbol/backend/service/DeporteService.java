package com.escuelaFutbol.backend.service;

import com.escuelaFutbol.backend.entity.Deporte;
import com.escuelaFutbol.backend.repository.DeporteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeporteService {

    private final DeporteRepository deporteRepository;

    public DeporteService(DeporteRepository deporteRepository) {
        this.deporteRepository = deporteRepository;
    }

    public List<Deporte> findAll() {
        return deporteRepository.findAll();
    }

    public Deporte save(Deporte deporte) {
        return deporteRepository.save(deporte);
    }
}
