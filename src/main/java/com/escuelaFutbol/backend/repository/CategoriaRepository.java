// src/main/java/com/escuelaFutbol/backend/repository/CategoriaRepository.java
package com.escuelaFutbol.backend.repository;

import com.escuelaFutbol.backend.entity.Categoria;
import com.escuelaFutbol.backend.entity.Deporte; // Asegúrate de que esta importación exista
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Si la usas en otros métodos

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Método para buscar categorías por nombre y deporte (para unicidad)
    Optional<Categoria> findByNombreAndDeporte(String nombre, Deporte deporte);

    // Método para buscar todas las categorías de un deporte específico
    List<Categoria> findByDeporte(Deporte deporte);

    // Método para contar categorías por deporte (ya sugerido, verifica que esté)
    long countByDeporte(Deporte deporte);
}