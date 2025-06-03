package com.escuelaFutbol.backend.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo; // Nueva importación
import com.fasterxml.jackson.annotation.ObjectIdGenerators; // Nueva importación


@Entity
@Table(name = "deportes")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id") // <--- AÑADIR ESTO: Usa el ID del Deporte para referenciarlo
public class Deporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @OneToMany(mappedBy = "deporte", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("deporte-categorias") // Mantiene el nombre
    private List<Categoria> categorias;

    @OneToMany(mappedBy = "deporte", fetch = FetchType.LAZY)
    @JsonManagedReference("deporte-jugadores")
    private List<Jugador> jugadores;

    // ... (El resto del código de Deporte.java: constructores, getters, setters, equals, hashCode, toString)

    public Deporte() { }

    public Deporte(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public List<Categoria> getCategorias() { return categorias; }
    public void setCategorias(List<Categoria> categorias) { this.categorias = categorias; }
    public List<Jugador> getJugadores() { return jugadores; }
    public void setJugadores(List<Jugador> jugadores) { this.jugadores = jugadores; }

    @Override
    public String toString() {
        return "Deporte{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deporte deporte = (Deporte) o;
        return id != null && Objects.equals(id, deporte.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : 0;
    }
}