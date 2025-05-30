package com.escuelaFutbol.backend.repository;

import com.escuelaFutbol.backend.entity.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JugadorRepository extends JpaRepository<Jugador, Long> {
}
