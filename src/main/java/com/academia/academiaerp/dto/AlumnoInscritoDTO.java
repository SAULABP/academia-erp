package com.academia.academiaerp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlumnoInscritoDTO {
    private Long inscripcionId;
    private Long alumnoId;
    private String nombreCompleto;
    private String dni;

    public AlumnoInscritoDTO(Long inscripcionId, Long alumnoId, String nombreCompleto, String dni) {
        this.inscripcionId = inscripcionId;
        this.alumnoId = alumnoId;
        this.nombreCompleto = nombreCompleto;
        this.dni = dni;
    }
}