package com.academia.academiaerp.controller;

import com.academia.academiaerp.dto.VentaRequestDTO;
import com.academia.academiaerp.dto.VentaResponseDTO;
import com.academia.academiaerp.service.VentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping
    public ResponseEntity<VentaResponseDTO> procesar(@RequestBody VentaRequestDTO dto) {
        VentaResponseDTO creada = ventaService.procesarVenta(dto);
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }

    @GetMapping
    public List<VentaResponseDTO> listar(@RequestParam(required = false) Long sedeId) {
        return ventaService.listar(sedeId);
    }

    @GetMapping("/{id}")
    public VentaResponseDTO obtener(@PathVariable Long id) {
        return ventaService.buscarPorId(id);
    }
}