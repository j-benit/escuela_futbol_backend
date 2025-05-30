package com.escuelaFutbol.backend.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Deporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @OneToMany(mappedBy = "deporte", cascade = CascadeType.ALL)
    private List<Categoria> categorias;

    // Getters y setters
}
