package com.academia.academiaerp.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ReporteResumenDTO {
    private String periodo;
    private BigDecimal ingresosCuotas;
    private BigDecimal ingresosVentas;
    private BigDecimal totalIngresos;
    private BigDecimal totalEgresos;
    private BigDecimal balance;
    private List<EgresoPorCategoriaDTO> egresosPorCategoria;

    public ReporteResumenDTO(String periodo, BigDecimal ingresosCuotas,
                             BigDecimal ingresosVentas, BigDecimal totalIngresos,
                             BigDecimal totalEgresos, BigDecimal balance,
                             List<EgresoPorCategoriaDTO> egresosPorCategoria) {
        this.periodo = periodo;
        this.ingresosCuotas = ingresosCuotas;
        this.ingresosVentas = ingresosVentas;
        this.totalIngresos = totalIngresos;
        this.totalEgresos = totalEgresos;
        this.balance = balance;
        this.egresosPorCategoria = egresosPorCategoria;
    }
}