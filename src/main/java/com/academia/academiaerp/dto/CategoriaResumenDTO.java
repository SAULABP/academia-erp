package com.academia.academiaerp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaResumenDTO {

    private Long id;
    private String nombre;

    public CategoriaResumenDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}