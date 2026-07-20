package com.academia.academiaerp.service;

import com.academia.academiaerp.dto.AlumnosPorSedeDTO;
import com.academia.academiaerp.dto.DashboardDTO;
import com.academia.academiaerp.dto.CuotaResponseDTO;
import com.academia.academiaerp.enums.EstadoCuota;
import com.academia.academiaerp.model.Alumno;
import com.academia.academiaerp.model.Cuota;
import com.academia.academiaerp.repository.AlumnoRepository;
import com.academia.academiaerp.repository.CuotaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final AlumnoRepository alumnoRepository;
    private final CuotaRepository cuotaRepository;
    private final ReporteService reporteService;

    public DashboardService(AlumnoRepository alumnoRepository,
                            CuotaRepository cuotaRepository,
                            ReporteService reporteService) {
        this.alumnoRepository = alumnoRepository;
        this.cuotaRepository = cuotaRepository;
        this.reporteService = reporteService;
    }

    public DashboardDTO obtener() {
        // Total de alumnos
        long totalAlumnos = alumnoRepository.count();

        // Cuotas pendientes o parciales (lo que falta cobrar)
        List<Cuota> pendientes = cuotaRepository.findAll().stream()
                .filter(c -> c.getEstado() == EstadoCuota.PENDIENTE
                        || c.getEstado() == EstadoCuota.PARCIAL
                        || c.getEstado() == EstadoCuota.VENCIDA)
                .toList();

        long cuotasPendientes = pendientes.size();
        BigDecimal montoPorCobrar = pendientes.stream()
                .map(c -> c.getMontoTotal().subtract(c.getMontoPagado()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Datos del mes actual (reutiliza el reporte)
        String periodoActual = YearMonth.now().toString();
        var reporte = reporteService.generarReporte(periodoActual);

        // Cuotas por vencer (próximas 5 pendientes, ordenadas por fecha)
        List<CuotaResponseDTO> porVencer = pendientes.stream()
                .sorted((a, b) -> a.getFechaVencimiento().compareTo(b.getFechaVencimiento()))
                .limit(5)
                .map(this::aDTO)
                .toList();
        // Alumnos agrupados por sede
        List<Alumno> todosAlumnos = alumnoRepository.findAll();
        Map<String, Long> conteoPorSede = todosAlumnos.stream()
                .filter(a -> a.getSede() != null)
                .collect(java.util.stream.Collectors.groupingBy(
                        a -> a.getSede().getNombre(),
                        java.util.stream.Collectors.counting()
                ));

        List<AlumnosPorSedeDTO> alumnosPorSede = conteoPorSede.entrySet().stream()
                .map(e -> new AlumnosPorSedeDTO(e.getKey(), e.getValue()))
                .sorted((a, b) -> Long.compare(b.getCantidad(), a.getCantidad())) // mayor a menor
                .toList();

        return new DashboardDTO(
                totalAlumnos,
                cuotasPendientes,
                montoPorCobrar,
                reporte.getTotalIngresos(),
                reporte.getTotalEgresos(),
                reporte.getBalance(),
                porVencer,
                alumnosPorSede   // <-- nuevo
        );
    }

    // Mapeo simple de Cuota a DTO (ajusta a como tengas tu CuotaResponseDTO)
    private CuotaResponseDTO aDTO(Cuota c) {
        CuotaResponseDTO dto = new CuotaResponseDTO();
        dto.setId(c.getId());
        dto.setPeriodo(c.getPeriodo());
        dto.setMontoTotal(c.getMontoTotal());
        dto.setMontoPagado(c.getMontoPagado());
        dto.setSaldoPendiente(c.getMontoTotal().subtract(c.getMontoPagado()));
        dto.setFechaVencimiento(c.getFechaVencimiento());
        dto.setEstado(c.getEstado());
        dto.setAlumnoId(c.getAlumno().getId());
        dto.setAlumnoNombreCompleto(c.getAlumno().getNombres() + " " + c.getAlumno().getApellidos());
        return dto;
    }
}