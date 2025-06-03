// src/main/java/com/escuelaFutbol/backend/repository/JugadorRepository.java
package com.escuelaFutbol.backend.repository;

import com.escuelaFutbol.backend.entity.Jugador;
import com.escuelaFutbol.backend.entity.Categoria; // Asegúrate de que esta importación exista
import com.escuelaFutbol.backend.entity.Deporte;   // Asegúrate de que esta importación exista
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List; // Si la usas en otros métodos

@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long> {
    // Método para buscar un jugador por su documento de identidad (asegurando unicidad)
    Optional<Jugador> findByDocumentoIdentidad(String documentoIdentidad);

    // Método para contar jugadores por categoría (ya sugerido, verifica que esté)
    long countByCategoria(Categoria categoria);

    // Método para contar jugadores por deporte (ya sugerido, verifica que esté)
    long countByDeporte(Deporte deporte);
}