package com.escuelaFutbol.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo; // Nueva importación
import com.fasterxml.jackson.annotation.ObjectIdGenerators; // Nueva importación

@Entity
@Table(name = "jugadores")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id") // <--- AÑADIR ESTO: Usa el ID del Jugador para referenciarlo
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(name = "documento_identidad", unique = true, length = 20)
    private String documentoIdentidad;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "telefono_contacto", length = 20)
    private String telefonoContacto;

    @Column(name = "email_contacto", length = 100)
    private String emailContacto;

    @Column(name = "foto_url", length = 255)
    private String fotoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    @JsonBackReference("categoria-jugadores")
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deporte_id", nullable = false)
    @JsonBackReference("deporte-jugadores")
    private Deporte deporte;

    // ... (El resto del código de Jugador.java, incluyendo getters y setters)

    public Jugador() { }

    public Jugador(String nombre, String apellido, String documentoIdentidad, LocalDate fechaNacimiento,
                   String telefonoContacto, String emailContacto, String fotoUrl, Categoria categoria, Deporte deporte) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.documentoIdentidad = documentoIdentidad;
        this.fechaNacimiento = fechaNacimiento;
        this.telefonoContacto = telefonoContacto;
        this.emailContacto = emailContacto;
        this.fotoUrl = fotoUrl;
        this.categoria = categoria;
        this.deporte = deporte;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getDocumentoIdentidad() { return documentoIdentidad; }
    public void setDocumentoIdentidad(String documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String telefonoContacto) { this.telefonoContacto = telefonoContacto; }
    public String getEmailContacto() { return emailContacto; }
    public void setEmailContacto(String emailContacto) {
        this.emailContacto = emailContacto;
    }
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public Deporte getDeporte() { return deporte; }
    public void setDeporte(Deporte deporte) { this.deporte = deporte; }

    @Override
    public String toString() {
        return "Jugador{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jugador jugador = (Jugador) o;
        return id != null && Objects.equals(id, jugador.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : 0;
    }
}