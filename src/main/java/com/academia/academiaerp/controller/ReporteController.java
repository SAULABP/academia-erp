package com.academia.academiaerp.controller;

import com.academia.academiaerp.dto.ReporteResumenDTO;
import com.academia.academiaerp.service.ReporteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/resumen")
    public ReporteResumenDTO resumen(@RequestParam String periodo) {
        return reporteService.generarReporte(periodo);
    }
}