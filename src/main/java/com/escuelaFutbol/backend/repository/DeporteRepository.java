package com.escuelaFutbol.backend.repository;

import com.escuelaFutbol.backend.entity.Deporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Importa esta anotación

import java.util.Optional; // ¡Importa Optional para este método!

@Repository // Marca esta interfaz como un componente de repositorio de Spring
public interface DeporteRepository extends JpaRepository<Deporte, Long> {

    /**
     * Busca un deporte por su nombre.
     * Spring Data JPA generará automáticamente la implementación de este método.
     * Se usa Optional para manejar el caso en que no se encuentre ningún deporte con el nombre dado.
     * @param nombre El nombre del deporte a buscar.
     * @return Un Optional que contiene el Deporte si es encontrado, o vacío si no.
     */
    Optional<Deporte> findByNombre(String nombre);
}