package com.academia.academiaerp.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ReporteResumenDTO {
    private String periodo;
    private BigDecimal ingresosMensualidad;
    private BigDecimal ingresosMatricula;
    private BigDecimal ingresosVentas;
    private BigDecimal totalIngresos;
    private BigDecimal totalEgresos;
    private BigDecimal balance;
    private List<EgresoPorCategoriaDTO> egresosPorCategoria;

    public ReporteResumenDTO(String periodo, BigDecimal ingresosMensualidad,
                             BigDecimal ingresosMatricula, BigDecimal ingresosVentas,
                             BigDecimal totalIngresos, BigDecimal totalEgresos,
                             BigDecimal balance, List<EgresoPorCategoriaDTO> egresosPorCategoria) {
        this.periodo = periodo;
        this.ingresosMensualidad = ingresosMensualidad;
        this.ingresosMatricula = ingresosMatricula;
        this.ingresosVentas = ingresosVentas;
        this.totalIngresos = totalIngresos;
        this.totalEgresos = totalEgresos;
        this.balance = balance;
        this.egresosPorCategoria = egresosPorCategoria;
    }
}