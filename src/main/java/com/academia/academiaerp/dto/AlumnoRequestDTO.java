package com.academia.academiaerp.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AlumnoRequestDTO {

    private String nombres;
    private String apellidos;
    private String dni;
    private Integer edad;
    private String telefono;
    private String fotoUrl;
    private BigDecimal cuotaMensual;

    private String apoderadoNombre;
    private String apoderadoTelefono;

    private Long sedeId;

    private List<Long> categoriaIds;
    private LocalDate fechaInicio;
    private BigDecimal montoMatricula;
}