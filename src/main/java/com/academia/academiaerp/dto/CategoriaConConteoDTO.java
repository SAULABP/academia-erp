package com.academia.academiaerp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaConConteoDTO {
    private Long id;
    private String nombre;
    private Integer edadMinima;
    private Integer edadMaxima;
    private Integer cupoMaximo;
    private String horario;
    private String deporteNombre;
    private String sedeNombre;
    private long alumnosInscritos;

    public CategoriaConConteoDTO(Long id, String nombre, Integer edadMinima, Integer edadMaxima,
                                 Integer cupoMaximo, String horario, String deporteNombre,
                                 String sedeNombre, long alumnosInscritos) {
        this.id = id;
        this.nombre = nombre;
        this.edadMinima = edadMinima;
        this.edadMaxima = edadMaxima;
        this.cupoMaximo = cupoMaximo;
        this.horario = horario;
        this.deporteNombre = deporteNombre;
        this.sedeNombre = sedeNombre;
        this.alumnosInscritos = alumnosInscritos;
    }
}