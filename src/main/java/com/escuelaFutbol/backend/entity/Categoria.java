package com.escuelaFutbol.backend.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "deporte_id")
    private Deporte deporte;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<Jugador> jugadores;

    // Getters y setters
}
