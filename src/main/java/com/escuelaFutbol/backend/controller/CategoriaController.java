package com.escuelaFutbol.backend.controller;

import com.escuelaFutbol.backend.entity.Categoria;
import com.escuelaFutbol.backend.service.CategoriaService;
import com.escuelaFutbol.backend.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> getAllCategorias() {
        List<Categoria> categorias = categoriaService.findAll();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Long id) {
        Categoria categoria = categoriaService.findById(id)
                                             .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
        return ResponseEntity.ok(categoria);
    }

    @PostMapping
    public ResponseEntity<Categoria> createCategoria(
            @RequestBody Categoria categoria,
            @RequestParam Long deporteId) {
        Categoria savedCategoria = categoriaService.save(categoria, deporteId);
        return new ResponseEntity<>(savedCategoria, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> updateCategoria(
            @PathVariable Long id,
            @RequestBody Categoria categoriaDetails,
            @RequestParam(required = false) Long newDeporteId) {
        Categoria updatedCategoria = categoriaService.update(id, categoriaDetails, newDeporteId);
        return ResponseEntity.ok(updatedCategoria);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        categoriaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-deporte/{deporteId}")
    public ResponseEntity<List<Categoria>> getCategoriasByDeporte(@PathVariable Long deporteId) {
        List<Categoria> categorias = categoriaService.findByDeporteId(deporteId);
        return ResponseEntity.ok(categorias);
    }
}