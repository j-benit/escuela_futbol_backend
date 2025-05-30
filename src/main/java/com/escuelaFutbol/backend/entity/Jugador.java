package com.escuelaFutbol.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String fotoUrl;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    // Aquí luego agregamos matrícula y demás relaciones

    // Getters y setters
}
