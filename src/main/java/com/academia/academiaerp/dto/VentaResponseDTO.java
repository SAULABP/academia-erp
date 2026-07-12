package com.academia.academiaerp.dto;

import com.academia.academiaerp.enums.MetodoPago;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class VentaResponseDTO {

    private Long id;
    private LocalDateTime fecha;
    private BigDecimal total;
    private MetodoPago metodoPago;
    private Long sedeId;
    private String sedeNombre;
    private List<DetalleVentaResponseDTO> detalles;
}