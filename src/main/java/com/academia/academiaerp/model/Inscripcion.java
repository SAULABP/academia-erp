package com.academia.academiaerp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(
        name = "inscripciones",
        uniqueConstraints = @UniqueConstraint(columnNames = {"alumno_id", "categoria_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    private LocalDate fechaInscripcion;

    private Boolean activa;
}