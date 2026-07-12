package com.academia.academiaerp.dto;

import com.academia.academiaerp.enums.MetodoPago;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VentaRequestDTO {

    private Long sedeId;
    private MetodoPago metodoPago;
    private List<DetalleVentaRequestDTO> items;
}