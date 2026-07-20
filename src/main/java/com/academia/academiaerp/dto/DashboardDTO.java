package com.academia.academiaerp.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class DashboardDTO {
    private long totalAlumnos;
    private long cuotasPendientes;
    private BigDecimal montoPorCobrar;
    private BigDecimal ingresosMes;
    private BigDecimal egresosMes;
    private BigDecimal balanceMes;
    private List<CuotaResponseDTO> cuotasPorVencer;
    private List<AlumnosPorSedeDTO> alumnosPorSede;

    public DashboardDTO(long totalAlumnos, long cuotasPendientes, BigDecimal montoPorCobrar,
                        BigDecimal ingresosMes, BigDecimal egresosMes, BigDecimal balanceMes,
                        List<CuotaResponseDTO> cuotasPorVencer,
                        List<AlumnosPorSedeDTO> alumnosPorSede) {
        this.totalAlumnos = totalAlumnos;
        this.cuotasPendientes = cuotasPendientes;
        this.montoPorCobrar = montoPorCobrar;
        this.ingresosMes = ingresosMes;
        this.egresosMes = egresosMes;
        this.balanceMes = balanceMes;
        this.cuotasPorVencer = cuotasPorVencer;
        this.alumnosPorSede = alumnosPorSede;
    }
}