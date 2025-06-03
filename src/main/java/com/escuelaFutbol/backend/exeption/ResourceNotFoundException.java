// src/main/java/com/escuelaFutbol/backend/exception/ResourceNotFoundException.java
package com.escuelaFutbol.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// @ResponseStatus(HttpStatus.NOT_FOUND) indica que cuando esta excepción sea lanzada,
// Spring responderá automáticamente con un código de estado HTTP 404 Not Found.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}