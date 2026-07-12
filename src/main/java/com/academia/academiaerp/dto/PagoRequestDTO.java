package com.academia.academiaerp.dto;

import com.academia.academiaerp.enums.MetodoPago;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PagoRequestDTO {

    private Long cuotaId;
    private BigDecimal monto;
    private MetodoPago metodoPago;
}