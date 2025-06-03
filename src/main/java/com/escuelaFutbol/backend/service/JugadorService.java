package com.escuelaFutbol.backend.service;

import com.escuelaFutbol.backend.entity.Categoria;
import com.escuelaFutbol.backend.entity.Deporte;
import com.escuelaFutbol.backend.entity.Jugador;
import com.escuelaFutbol.backend.exception.ResourceNotFoundException;
import com.escuelaFutbol.backend.repository.CategoriaRepository;
import com.escuelaFutbol.backend.repository.DeporteRepository;
import com.escuelaFutbol.backend.repository.JugadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class JugadorService {

    private final JugadorRepository jugadorRepository;
    private final CategoriaRepository categoriaRepository;
    private final DeporteRepository deporteRepository;

    // Constructor para inyección de dependencias
    public JugadorService(JugadorRepository jugadorRepository,
                          CategoriaRepository categoriaRepository,
                          DeporteRepository deporteRepository) {
        this.jugadorRepository = jugadorRepository;
        this.categoriaRepository = categoriaRepository;
        this.deporteRepository = deporteRepository;
    }

    /**
     * Obtiene todos los jugadores.
     * @return Lista de todos los jugadores.
     */
    public List<Jugador> findAll() {
        return jugadorRepository.findAll();
    }

    /**
     * Busca un jugador por su ID.
     * @param id ID del jugador.
     * @return Un Optional que contiene el jugador si se encuentra, o vacío si no.
     */
    public Optional<Jugador> findById(Long id) {
        return jugadorRepository.findById(id);
    }

    /**
     * Guarda un nuevo jugador, asignándole un deporte y categoría.
     * @param jugador El objeto Jugador a guardar.
     * @param deporteId El ID del deporte al que pertenece el jugador.
     * @return El jugador guardado.
     * @throws ResourceNotFoundException si el deporte no existe.
     * @throws IllegalArgumentException si el documento de identidad ya está en uso.
     */
    @Transactional
    public Jugador save(Jugador jugador, Long deporteId) {
        // Validar el jugador antes de cualquier operación
        //jugador.validate();

        // Verificar si el documento de identidad ya existe
        if (jugadorRepository.findByDocumentoIdentidad(jugador.getDocumentoIdentidad()).isPresent()) {
            throw new IllegalArgumentException("El documento de identidad '" + jugador.getDocumentoIdentidad() + "' ya está en uso.");
        }

        // Buscar y asignar el deporte
        Deporte deporte = deporteRepository.findById(deporteId)
                .orElseThrow(() -> new ResourceNotFoundException("Deporte no encontrado con ID: " + deporteId));
        jugador.setDeporte(deporte);

        // Asignar categoría basada en la edad y el deporte
        asignarCategoriaPorEdad(jugador, deporte);

        return jugadorRepository.save(jugador);
    }

    /**
     * Actualiza un jugador existente.
     * @param id ID del jugador a actualizar.
     * @param jugadorDetails Objeto Jugador con los datos actualizados.
     * @param nuevoDeporteId (Opcional) ID del nuevo deporte para reasignar al jugador.
     * @return El jugador actualizado.
     * @throws ResourceNotFoundException si el jugador o el nuevo deporte no se encuentran.
     * @throws IllegalArgumentException si el documento de identidad ya está en uso o datos inválidos.
     */
    @Transactional
    public Jugador update(Long id, Jugador jugadorDetails, Long nuevoDeporteId) {
        Jugador jugadorExistente = jugadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jugador no encontrado con ID: " + id));

        // Actualizar campos básicos
        jugadorExistente.setNombre(jugadorDetails.getNombre());
        jugadorExistente.setApellido(jugadorDetails.getApellido());
        jugadorExistente.setFechaNacimiento(jugadorDetails.getFechaNacimiento());
        jugadorExistente.setFotoUrl(jugadorDetails.getFotoUrl());
        jugadorExistente.setTelefonoContacto(jugadorDetails.getTelefonoContacto());
        jugadorExistente.setEmailContacto(jugadorDetails.getEmailContacto());

        // Manejar actualización de documento de identidad (asegurando unicidad)
        if (jugadorDetails.getDocumentoIdentidad() != null && !jugadorDetails.getDocumentoIdentidad().equals(jugadorExistente.getDocumentoIdentidad())) {
            Optional<Jugador> existingWithDoc = jugadorRepository.findByDocumentoIdentidad(jugadorDetails.getDocumentoIdentidad());
            if (existingWithDoc.isPresent() && !existingWithDoc.get().getId().equals(id)) {
                throw new IllegalArgumentException("El nuevo documento de identidad ya está asociado a otro jugador.");
            }
            jugadorExistente.setDocumentoIdentidad(jugadorDetails.getDocumentoIdentidad());
        }

        // Lógica para manejar el cambio de deporte (si se proporciona nuevoDeporteId)
        Deporte deporteActualDelJugador = jugadorExistente.getDeporte();
        if (nuevoDeporteId != null) {
            Deporte deporteNuevo = deporteRepository.findById(nuevoDeporteId)
                    .orElseThrow(() -> new ResourceNotFoundException("El Deporte con ID " + nuevoDeporteId + " no fue encontrado para la actualización del jugador."));

            if (!deporteNuevo.getId().equals(deporteActualDelJugador.getId())) {
                jugadorExistente.setDeporte(deporteNuevo);
                // Si el deporte cambia, recalcula la categoría para el nuevo deporte
                asignarCategoriaPorEdad(jugadorExistente, deporteNuevo);
            } else {
                // Si el deporte no cambia, pero se recalcula la categoría por si la edad o reglas cambiaron
                asignarCategoriaPorEdad(jugadorExistente, deporteActualDelJugador);
            }
        } else {
            // Si no se proporciona nuevoDeporteId, solo revalida la categoría si la fecha de nacimiento cambió
            asignarCategoriaPorEdad(jugadorExistente, deporteActualDelJugador);
        }

        // Validar la entidad actualizada (esto validará todos los campos, incluida la categoría)
        //jugadorExistente.validate();

        return jugadorRepository.save(jugadorExistente);
    }

    /**
     * Elimina un jugador por su ID.
     * @param id ID del jugador a eliminar.
     * @throws ResourceNotFoundException si el jugador no se encuentra.
     */
    @Transactional
    public void deleteById(Long id) {
        if (!jugadorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Jugador no encontrado con ID: " + id);
        }
        jugadorRepository.deleteById(id);
    }

    /**
     * Método auxiliar para asignar la categoría de un jugador según su edad y el deporte.
     * Este método también puede ser usado para reasignar la categoría si cambian las reglas de edad o el deporte.
     * @param jugador El jugador al que se le asignará la categoría.
     * @param deporte El deporte al que el jugador está asociado (necesario para buscar categorías).
     * @throws IllegalArgumentException si el deporte o la fecha de nacimiento del jugador son nulos.
     * @throws ResourceNotFoundException si no se encuentra una categoría adecuada para la edad y el deporte.
     */
    private void asignarCategoriaPorEdad(Jugador jugador, Deporte deporte) {
        if (deporte == null) {
            throw new IllegalArgumentException("El deporte no puede ser nulo para asignar una categoría.");
        }
        if (jugador.getFechaNacimiento() == null) {
            throw new IllegalArgumentException("La fecha de nacimiento del jugador es necesaria para asignar una categoría.");
        }

        // Calcular la edad del jugador
        int edadJugador = Period.between(jugador.getFechaNacimiento(), LocalDate.now()).getYears();

        // Buscar todas las categorías para el deporte específico
        // Asegúrate que CategoriaRepository tiene el método findByDeporte(Deporte deporte)
        List<Categoria> categoriasDelDeporte = categoriaRepository.findByDeporte(deporte);

        Optional<Categoria> categoriaEncontrada = categoriasDelDeporte.stream()
                .filter(cat -> edadJugador >= cat.getEdadMinima() && edadJugador <= cat.getEdadMaxima())
                .findFirst();

        jugador.setCategoria(categoriaEncontrada.orElseThrow(
                () -> new ResourceNotFoundException("No se encontró una categoría adecuada para la edad " + edadJugador +
                        " para el deporte '" + deporte.getNombre() + "'.")));
    }
}