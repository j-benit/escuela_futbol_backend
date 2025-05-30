package com.escuelaFutbol.backend.repository;

import com.escuelaFutbol.backend.entity.Deporte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeporteRepository extends JpaRepository<Deporte, Long> {
}
