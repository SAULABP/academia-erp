package com.academia.academiaerp.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DetalleVentaResponseDTO {

    private Long productoId;
    private String productoNombre;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}