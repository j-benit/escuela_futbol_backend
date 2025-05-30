package com.escuelaFutbol.backend.repository;

import com.escuelaFutbol.backend.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
