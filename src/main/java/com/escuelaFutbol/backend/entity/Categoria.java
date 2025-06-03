package com.escuelaFutbol.backend.entity;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference; // Volvemos a importar
import com.fasterxml.jackson.annotation.JsonIdentityInfo; // Nueva importación
import com.fasterxml.jackson.annotation.ObjectIdGenerators; // Nueva importación


@Entity
@Table(name = "categorias")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id") // <--- AÑADIR ESTO: Usa el ID de la Categoria para referenciarla
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(name = "edad_minima", nullable = false)
    private Integer edadMinima;

    @Column(name = "edad_maxima", nullable = false)
    private Integer edadMaxima;

    @Column(length = 255)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deporte_id", nullable = false)
    @JsonBackReference("deporte-categorias") // <--- Volvemos a poner JsonBackReference aquí, pero ahora con un nombre
    private Deporte deporte;

    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    @JsonManagedReference("categoria-jugadores")
    private List<Jugador> jugadores;

    // ... (El resto del código de Categoria.java: constructores, getters, setters, equals, hashCode, toString)

    public void validate() {
        if (this.nombre == null || this.nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío.");
        }
        if (this.edadMinima == null || this.edadMinima < 0) {
            throw new IllegalArgumentException("La edad mínima debe ser un número positivo.");
        }
        if (this.edadMaxima == null || this.edadMaxima < 0) {
            throw new IllegalArgumentException("La edad máxima debe ser un número positivo.");
        }
        if (this.edadMinima > this.edadMaxima) {
            throw new IllegalArgumentException("La edad mínima no puede ser mayor que la edad máxima.");
        }
    }

    public Categoria() { }

    public Categoria(String nombre, Integer edadMinima, Integer edadMaxima, String descripcion, Deporte deporte) {
        this.nombre = nombre;
        this.edadMinima = edadMinima;
        this.edadMaxima = edadMaxima;
        this.descripcion = descripcion;
        this.deporte = deporte;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Integer getEdadMinima() { return edadMinima; }
    public void setEdadMinima(Integer edadMinima) { this.edadMinima = edadMinima; }
    public Integer getEdadMaxima() { return edadMaxima; }
    public void setEdadMaxima(Integer edadMaxima) { this.edadMaxima = edadMaxima; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Deporte getDeporte() { return deporte; }
    public void setDeporte(Deporte deporte) { this.deporte = deporte; }
    public List<Jugador> getJugadores() { return jugadores; }
    public void setJugadores(List<Jugador> jugadores) { this.jugadores = jugadores; }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", edadMinima=" + edadMinima +
                ", edadMaxima=" + edadMaxima +
                ", deporte=" + (deporte != null ? deporte.getNombre() : "N/A") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return id != null && Objects.equals(id, categoria.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : 0;
    }
}