package com.academia.academiaerp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "alumnos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false, unique = true)
    private String dni;

    private Integer edad;

    private String telefono;

    private String fotoUrl;

    @Column(nullable = false)
    private BigDecimal cuotaMensual;

    // Datos del apoderado (campos planos)
    private String apoderadoNombre;

    private String apoderadoTelefono;

    // Relación: cada alumno pertenece a UNA sede fija
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sede_id", nullable = false)
    private Sede sede;
}