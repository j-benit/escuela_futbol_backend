package com.escuelaFutbol.backend.service;

import com.escuelaFutbol.backend.entity.Categoria;
import com.escuelaFutbol.backend.entity.Deporte;
import com.escuelaFutbol.backend.exception.ResourceNotFoundException;
import com.escuelaFutbol.backend.repository.CategoriaRepository;
import com.escuelaFutbol.backend.repository.DeporteRepository;
import com.escuelaFutbol.backend.repository.JugadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final DeporteRepository deporteRepository;
    private final JugadorRepository jugadorRepository;

    public CategoriaService(CategoriaRepository categoriaRepository,
                            DeporteRepository deporteRepository,
                            JugadorRepository jugadorRepository) {
        this.categoriaRepository = categoriaRepository;
        this.deporteRepository = deporteRepository;
        this.jugadorRepository = jugadorRepository;
    }

    @Transactional(readOnly = true)
    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Categoria> findById(Long id) {
        return categoriaRepository.findById(id);
    }

    @Transactional
    public Categoria save(Categoria categoria, Long deporteId) {
        // 1. Buscar y asignar el Deporte PRIMERO
        Deporte deporte = deporteRepository.findById(deporteId)
            .orElseThrow(() -> new ResourceNotFoundException("Deporte no encontrado con ID: " + deporteId));
        categoria.setDeporte(deporte);

        // 2. Validar campos básicos de la categoría AHORA que el deporte está asignado
        categoria.validate();

        // 3. Verificar si ya existe una categoría con el mismo nombre y deporte
        if (categoriaRepository.findByNombreAndDeporte(categoria.getNombre(), deporte).isPresent()) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre '" + categoria.getNombre() + "' para el deporte '" + deporte.getNombre() + "'.");
        }

        // 4. Guardar la categoría
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public Categoria update(Long id, Categoria categoriaDetails, Long newDeporteId) {
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));

        categoriaExistente.setNombre(categoriaDetails.getNombre());
        categoriaExistente.setEdadMinima(categoriaDetails.getEdadMinima());
        categoriaExistente.setEdadMaxima(categoriaDetails.getEdadMaxima());
        categoriaExistente.setDescripcion(categoriaDetails.getDescripcion());

        Deporte deporteActualizado = categoriaExistente.getDeporte();
        if (newDeporteId != null && !newDeporteId.equals(categoriaExistente.getDeporte().getId())) {
            deporteActualizado = deporteRepository.findById(newDeporteId)
                .orElseThrow(() -> new ResourceNotFoundException("Deporte no encontrado con ID: " + newDeporteId));
            categoriaExistente.setDeporte(deporteActualizado);
        }

        categoriaExistente.validate();

        Optional<Categoria> existingConflict = categoriaRepository.findByNombreAndDeporte(categoriaExistente.getNombre(), categoriaExistente.getDeporte());
        if (existingConflict.isPresent() && !existingConflict.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe otra categoría con el nombre '" + categoriaExistente.getNombre() + "' para el deporte '" + categoriaExistente.getDeporte().getNombre() + "'.");
        }

        return categoriaRepository.save(categoriaExistente);
    }

    @Transactional
    public void deleteById(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));

        long jugadoresCount = jugadorRepository.countByCategoria(categoria);
        if (jugadoresCount > 0) {
            throw new IllegalStateException("No se puede eliminar la categoría '" + categoria.getNombre() +
                                             "' porque tiene " + jugadoresCount + " jugador(es) asociado(s). " +
                                             "Primero reasigne o elimine los jugadores.");
        }

        categoriaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Categoria> findByDeporteId(Long deporteId) {
        Deporte deporte = deporteRepository.findById(deporteId)
            .orElseThrow(() -> new ResourceNotFoundException("Deporte no encontrado con ID: " + deporteId));
        return categoriaRepository.findByDeporte(deporte);
    }
}