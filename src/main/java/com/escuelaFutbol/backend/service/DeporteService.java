package com.escuelaFutbol.backend.service;

import com.escuelaFutbol.backend.entity.Deporte;
import com.escuelaFutbol.backend.exception.ResourceNotFoundException;
import com.escuelaFutbol.backend.repository.CategoriaRepository; // Para validar si hay categorías asociadas
import com.escuelaFutbol.backend.repository.DeporteRepository;
import com.escuelaFutbol.backend.repository.JugadorRepository;   // Para validar si hay jugadores asociados
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Para el manejo de transacciones

import java.util.List;
import java.util.Optional;

@Service
public class DeporteService {

    private final DeporteRepository deporteRepository;
    private final CategoriaRepository categoriaRepository; // Inyectamos CategoriaRepository
    private final JugadorRepository jugadorRepository;     // Inyectamos JugadorRepository

    @Autowired // Spring se encarga de inyectar las dependencias
    public DeporteService(DeporteRepository deporteRepository,
                          CategoriaRepository categoriaRepository,
                          JugadorRepository jugadorRepository) {
        this.deporteRepository = deporteRepository;
        this.categoriaRepository = categoriaRepository;
        this.jugadorRepository = jugadorRepository;
    }

    /**
     * Obtiene una lista de todos los deportes almacenados en la base de datos.
     *
     * @return Una lista de objetos Deporte.
     */
    @Transactional(readOnly = true) // Optimización para operaciones de solo lectura
    public List<Deporte> findAll() {
        return deporteRepository.findAll();
    }

    /**
     * Busca un deporte por su ID.
     *
     * @param id El ID del deporte a buscar.
     * @return Un Optional que contiene el Deporte si es encontrado, o vacío si no.
     */
    @Transactional(readOnly = true)
    public Optional<Deporte> findById(Long id) {
        return deporteRepository.findById(id);
    }

    /**
     * Guarda un nuevo deporte en la base de datos.
     * Realiza validaciones de negocio para asegurar la unicidad del nombre.
     *
     * @param deporte El objeto Deporte a guardar.
     * @return El Deporte guardado con su ID.
     * @throws IllegalArgumentException si el nombre del deporte ya existe o es inválido.
     */
    @Transactional // Esta operación modifica la base de datos
    public Deporte save(Deporte deporte) {
        // 1. Validar campos básicos usando el método validate() de la entidad

        // 2. Verificar si ya existe un deporte con el mismo nombre (unicidad)
        Optional<Deporte> existingDeporte = deporteRepository.findByNombre(deporte.getNombre());
        if (existingDeporte.isPresent()) {
            throw new IllegalArgumentException("Ya existe un deporte con el nombre: " + deporte.getNombre());
        }

        // 3. Guardar el deporte
        return deporteRepository.save(deporte);
    }

    /**
     * Actualiza un deporte existente.
     *
     * @param id El ID del deporte a actualizar.
     * @param deporteDetails Los nuevos detalles del deporte.
     * @return El Deporte actualizado.
     * @throws ResourceNotFoundException si el deporte no es encontrado.
     * @throws IllegalArgumentException si los datos del deporte son inválidos o el nombre ya existe (para otro deporte).
     */
    @Transactional
    public Deporte update(Long id, Deporte deporteDetails) {
        Deporte deporteExistente = deporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deporte no encontrado con ID: " + id));

        // 1. Actualizar campos
        deporteExistente.setNombre(deporteDetails.getNombre());
        deporteExistente.setDescripcion(deporteDetails.getDescripcion());

        // 2. Validar la entidad actualizada (esto validará que el nombre no sea nulo/vacío)
       // deporteExistente.validate();

        // 3. Verificar si el nuevo nombre ya existe en otro deporte
        Optional<Deporte> existingWithNewName = deporteRepository.findByNombre(deporteExistente.getNombre());
        if (existingWithNewName.isPresent() && !existingWithNewName.get().getId().equals(id)) {
            throw new IllegalArgumentException("El nombre '" + deporteExistente.getNombre() + "' ya está en uso por otro deporte.");
        }

        // 4. Guardar el deporte actualizado
        return deporteRepository.save(deporteExistente);
    }

    /**
     * Elimina un deporte por su ID.
     * Antes de eliminar, verifica si hay categorías o jugadores asociados a este deporte.
     *
     * @param id El ID del deporte a eliminar.
     * @throws ResourceNotFoundException si el deporte no es encontrado.
     * @throws IllegalStateException si el deporte tiene categorías o jugadores asociados.
     */
    @Transactional
    public void deleteById(Long id) {
        Deporte deporte = deporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deporte no encontrado con ID: " + id));

        // 1. Verificar si existen categorías asociadas a este deporte
        long categoriasCount = categoriaRepository.countByDeporte(deporte); // Asume este método en CategoriaRepository
        if (categoriasCount > 0) {
            throw new IllegalStateException("No se puede eliminar el deporte '" + deporte.getNombre() +
                                            "' porque tiene " + categoriasCount + " categoría(s) asociada(s). " +
                                            "Primero elimine o reasigne las categorías.");
        }

        // 2. Verificar si existen jugadores asociados a este deporte
        long jugadoresCount = jugadorRepository.countByDeporte(deporte); // Asume este método en JugadorRepository
        if (jugadoresCount > 0) {
            throw new IllegalStateException("No se puede eliminar el deporte '" + deporte.getNombre() +
                                            "' porque tiene " + jugadoresCount + " jugador(es) asociado(s). " +
                                            "Primero elimine o reasigne los jugadores.");
        }

        // Si no hay categorías ni jugadores, se puede eliminar el deporte
        deporteRepository.deleteById(id);
    }
}