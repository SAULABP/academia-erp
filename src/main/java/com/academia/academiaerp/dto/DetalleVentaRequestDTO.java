package com.academia.academiaerp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalleVentaRequestDTO {

    private Long productoId;
    private Integer cantidad;
}