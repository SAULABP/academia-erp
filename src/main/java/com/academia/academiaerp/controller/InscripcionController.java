package com.academia.academiaerp.controller;

import com.academia.academiaerp.dto.AlumnoInscritoDTO;
import com.academia.academiaerp.service.InscripcionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inscripciones")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    public InscripcionController(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @GetMapping("/categoria/{categoriaId}/inscritos")
    public List<AlumnoInscritoDTO> listarInscritos(@PathVariable Long categoriaId) {
        return inscripcionService.listarInscritos(categoriaId);
    }

    @PutMapping("/{inscripcionId}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desactivar(@PathVariable Long inscripcionId) {
        inscripcionService.desactivar(inscripcionId);
        return ResponseEntity.noContent().build();
    }
}