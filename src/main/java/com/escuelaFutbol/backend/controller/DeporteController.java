package com.escuelaFutbol.backend.controller;

import com.escuelaFutbol.backend.entity.Deporte;
import com.escuelaFutbol.backend.service.DeporteService;
import com.escuelaFutbol.backend.exception.ResourceNotFoundException; // Importa tu clase de excepción
import org.springframework.http.HttpStatus; // Para códigos de estado HTTP
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/deportes") // Define la ruta base para todos los endpoints de este controlador
public class DeporteController {

    private final DeporteService deporteService;

    // Inyección de dependencias a través del constructor, es la forma recomendada por Spring
    public DeporteController(DeporteService deporteService) {
        this.deporteService = deporteService;
    }

    /**
     * Obtiene una lista de todos los deportes registrados.
     * GET /api/deportes
     *
     * @return ResponseEntity con una lista de objetos Deporte y el estado HTTP 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<Deporte>> getAllDeportes() {
        List<Deporte> deportes = deporteService.findAll();
        return ResponseEntity.ok(deportes); // Retorna 200 OK con la lista de deportes
    }

    /**
     * Obtiene un deporte específico por su ID.
     * GET /api/deportes/{id}
     *
     * @param id El ID del deporte a buscar, extraído de la URL.
     * @return ResponseEntity con el objeto Deporte encontrado y el estado HTTP 200 OK,
     * o un estado HTTP 404 Not Found si el deporte no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Deporte> getDeporteById(@PathVariable Long id) {
        // Usa orElseThrow para lanzar ResourceNotFoundException si el deporte no se encuentra.
        // La anotación @ResponseStatus en ResourceNotFoundException se encargará del 404.
        Deporte deporte = deporteService.findById(id)
                                        .orElseThrow(() -> new ResourceNotFoundException("Deporte no encontrado con ID: " + id));
        return ResponseEntity.ok(deporte); // Retorna 200 OK con el deporte encontrado
    }

    /**
     * Crea un nuevo deporte.
     * POST /api/deportes
     *
     * @param deporte El objeto Deporte enviado en el cuerpo de la petición.
     * @return ResponseEntity con el deporte creado y el estado HTTP 201 Created si la creación fue exitosa,
     * o un estado HTTP 400 Bad Request si los datos son inválidos o el nombre ya existe.
     */
    @PostMapping
    public ResponseEntity<Deporte> createDeporte(@RequestBody Deporte deporte) {
        try {
            Deporte savedDeporte = deporteService.save(deporte);
            // Retorna 201 Created y el deporte guardado en el cuerpo de la respuesta
            return new ResponseEntity<>(savedDeporte, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Captura errores de validación de negocio (ej. nombre duplicado o vacío)
            // Aquí podríamos retornar un objeto DTO de error más específico con el mensaje de 'e.getMessage()'
            return ResponseEntity.badRequest().body(null); // Retorna 400 Bad Request
            // Ejemplo de respuesta más completa:
            // return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Actualiza un deporte existente por su ID.
     * PUT /api/deportes/{id}
     *
     * @param id El ID del deporte a actualizar, extraído de la URL.
     * @param deporteDetails El objeto Deporte con los detalles actualizados, enviado en el cuerpo de la petición.
     * @return ResponseEntity con el deporte actualizado y el estado HTTP 200 OK si la actualización fue exitosa,
     * o un estado HTTP 404 Not Found si el deporte no existe,
     * o un estado HTTP 400 Bad Request si los datos son inválidos o el nombre ya está en uso.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Deporte> updateDeporte(@PathVariable Long id, @RequestBody Deporte deporteDetails) {
        try {
            Deporte updatedDeporte = deporteService.update(id, deporteDetails);
            return ResponseEntity.ok(updatedDeporte); // Retorna 200 OK con el deporte actualizado
        } catch (ResourceNotFoundException e) {
            // Se propaga la excepción, y @ResponseStatus en ResourceNotFoundException la manejará como 404
            throw e;
        } catch (IllegalArgumentException e) {
            // Captura errores de validación (ej. nombre duplicado o inválido)
            return ResponseEntity.badRequest().body(null); // Retorna 400 Bad Request
        }
    }

    /**
     * Elimina un deporte por su ID.
     * DELETE /api/deportes/{id}
     *
     * @param id El ID del deporte a eliminar, extraído de la URL.
     * @return ResponseEntity con el estado HTTP 204 No Content si la eliminación fue exitosa,
     * o un estado HTTP 404 Not Found si el deporte no existe,
     * o un estado HTTP 400 Bad Request si el deporte tiene categorías o jugadores asociados.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeporte(@PathVariable Long id) {
        try {
            deporteService.deleteById(id);
            // Retorna 204 No Content, que indica que la petición se completó exitosamente sin contenido para retornar
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            // Se propaga la excepción, y @ResponseStatus en ResourceNotFoundException la manejará como 404
            throw e;
        } catch (IllegalStateException e) {
            // Captura el error si el deporte tiene categorías o jugadores asociados
            return ResponseEntity.badRequest().body(null); // Retorna 400 Bad Request
            // En un escenario real, enviarías un mensaje de error más descriptivo:
            // return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage()));
        }
    }
}