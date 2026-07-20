package com.academia.academiaerp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlumnosPorSedeDTO {
    private String sedeNombre;
    private long cantidad;

    public AlumnosPorSedeDTO(String sedeNombre, long cantidad) {
        this.sedeNombre = sedeNombre;
        this.cantidad = cantidad;
    }
}