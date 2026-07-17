package com.academia.academiaerp.controller;

import com.academia.academiaerp.dto.CuotaResponseDTO;
import com.academia.academiaerp.dto.PagoRequestDTO;
import com.academia.academiaerp.enums.EstadoCuota;
import com.academia.academiaerp.service.CuotaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cuotas")
public class CuotaController {

    private final CuotaService cuotaService;

    public CuotaController(CuotaService cuotaService) {
        this.cuotaService = cuotaService;
    }

    @GetMapping
    public List<CuotaResponseDTO> listar(
            @RequestParam(required = false) Long alumnoId,
            @RequestParam(required = false) EstadoCuota estado) {
        return cuotaService.listar(alumnoId, estado);
    }

    @PostMapping("/generar")
    public ResponseEntity<CuotaResponseDTO> generarCuota(
            @RequestParam Long alumnoId,
            @RequestParam String periodo,
            @RequestParam LocalDate fechaVencimiento) {
        CuotaResponseDTO creada = cuotaService.generarCuota(alumnoId, periodo, fechaVencimiento);
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }
    @PostMapping("/generar-masivo")
    public List<CuotaResponseDTO> generarMasivo(@RequestParam String periodo) {
        return cuotaService.generarCuotasMasivas(periodo);
    }

    @PostMapping("/pagar")
    public CuotaResponseDTO registrarPago(@RequestBody PagoRequestDTO dto) {
        return cuotaService.registrarPago(dto);
    }
}