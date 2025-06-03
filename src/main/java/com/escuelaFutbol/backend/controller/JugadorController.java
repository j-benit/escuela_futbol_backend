package com.escuelaFutbol.backend.controller;

import com.escuelaFutbol.backend.entity.Jugador;
import com.escuelaFutbol.backend.service.JugadorService;
import com.escuelaFutbol.backend.exception.ResourceNotFoundException; // ¡Asegúrate de que esta clase exista!
import org.springframework.http.HttpStatus; // Para los códigos de estado HTTP
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/jugadores") // Define la ruta base para todas las operaciones en este controlador
public class JugadorController {

    private final JugadorService jugadorService;

    // Inyección de dependencias a través del constructor (recomendado por Spring)
    public JugadorController(JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    /**
     * Obtiene una lista de todos los jugadores.
     * GET /api/jugadores
     * @return ResponseEntity con una lista de jugadores y HttpStatus.OK.
     */
    @GetMapping
    public ResponseEntity<List<Jugador>> getAllJugadores() {
        List<Jugador> jugadores = jugadorService.findAll();
        return ResponseEntity.ok(jugadores); // Retorna 200 OK con la lista
    }

    /**
     * Obtiene un jugador por su ID.
     * GET /api/jugadores/{id}
     * @param id El ID del jugador a buscar.
     * @return ResponseEntity con el jugador encontrado y HttpStatus.OK, o HttpStatus.NOT_FOUND si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Jugador> getJugadorById(@PathVariable Long id) {
        // Usa orElseThrow para lanzar ResourceNotFoundException si el jugador no se encuentra
        Jugador jugador = jugadorService.findById(id)
                                        .orElseThrow(() -> new ResourceNotFoundException("Jugador no encontrado con ID: " + id));
        return ResponseEntity.ok(jugador); // Retorna 200 OK con el jugador
    }

    /**
     * Crea un nuevo jugador.
     * POST /api/jugadores?deporteId={deporteId}
     *
     * @param jugador El objeto Jugador enviado en el cuerpo de la petición.
     * @param deporteId El ID del deporte al que pertenece el jugador, recibido como parámetro de consulta.
     * @return ResponseEntity con el jugador creado y HttpStatus.CREATED, o un error si la validación falla.
     */
    @PostMapping
    public ResponseEntity<Jugador> createJugador(
            @RequestBody Jugador jugador,
            @RequestParam Long deporteId) { // Aquí recibimos el deporteId como un parámetro de consulta
        try {
            // Llama al servicio pasando tanto el objeto Jugador como el deporteId
            Jugador savedJugador = jugadorService.save(jugador, deporteId);
            return new ResponseEntity<>(savedJugador, HttpStatus.CREATED); // Retorna 201 Created
        } catch (IllegalArgumentException e) {
            // Captura errores de validación de negocio (ej. documento duplicado, datos incompletos)
            // Podrías retornar un DTO de error más específico aquí
            return ResponseEntity.badRequest().body(null); // Retorna 400 Bad Request
            // En un escenario real, enviarías un mensaje de error más descriptivo:
            // return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Actualiza un jugador existente por su ID.
     * PUT /api/jugadores/{id}?deporteId={nuevoDeporteId}
     *
     * @param id El ID del jugador a actualizar.
     * @param jugadorDetails Los nuevos detalles del jugador.
     * @param nuevoDeporteId (Opcional) El nuevo ID del deporte para reasignar al jugador.
     * @return ResponseEntity con el jugador actualizado y HttpStatus.OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Jugador> updateJugador(
            @PathVariable Long id,
            @RequestBody Jugador jugadorDetails,
            @RequestParam(required = false) Long nuevoDeporteId) { // nuevoDeporteId es opcional
        try {
            Jugador updatedJugador = jugadorService.update(id, jugadorDetails, nuevoDeporteId);
            return ResponseEntity.ok(updatedJugador); // Retorna 200 OK
        } catch (ResourceNotFoundException e) {
            throw e; // ResourceNotFoundException ya tiene @ResponseStatus(HttpStatus.NOT_FOUND)
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Captura errores de validación o de estado de negocio
            return ResponseEntity.badRequest().body(null); // Retorna 400 Bad Request
            // En un escenario real, enviarías un mensaje de error más descriptivo
        }
    }

    /**
     * Elimina un jugador por su ID.
     * DELETE /api/jugadores/{id}
     * @param id El ID del jugador a eliminar.
     * @return ResponseEntity con HttpStatus.NO_CONTENT si la eliminación fue exitosa.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJugador(@PathVariable Long id) {
        try {
            jugadorService.deleteById(id);
            return ResponseEntity.noContent().build(); // Retorna 204 No Content
        } catch (ResourceNotFoundException e) {
            throw e; // ResourceNotFoundException ya tiene @ResponseStatus(HttpStatus.NOT_FOUND)
        }
    }
}