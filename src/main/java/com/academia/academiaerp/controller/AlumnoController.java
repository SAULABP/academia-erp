package com.academia.academiaerp.controller;

import com.academia.academiaerp.dto.AlumnoRequestDTO;
import com.academia.academiaerp.dto.AlumnoResponseDTO;
import com.academia.academiaerp.service.AlumnoService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alumnos")
public class AlumnoController {

    private final AlumnoService alumnoService;

    public AlumnoController(AlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }

    @GetMapping
    public List<AlumnoResponseDTO> listar(@RequestParam(required = false) Long sedeId) {
        return alumnoService.listar(sedeId);
    }

    @GetMapping("/buscar")
    public List<AlumnoResponseDTO> buscar(@RequestParam String texto) {
        return alumnoService.buscarPorTexto(texto);
    }

    @GetMapping("/{id}")
    public AlumnoResponseDTO obtener(@PathVariable Long id) {
        return alumnoService.buscarPorId(id);
    }



    @PostMapping
    public ResponseEntity<AlumnoResponseDTO> crear(@RequestBody AlumnoRequestDTO dto) {
        AlumnoResponseDTO creado = alumnoService.crear(dto);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }
    @PostMapping("/{id}/foto")
    public AlumnoResponseDTO subirFoto(
            @PathVariable Long id,
            @RequestParam("archivo") MultipartFile archivo) {
        return alumnoService.subirFoto(id, archivo);
    }

    @PutMapping("/{id}")
    public AlumnoResponseDTO actualizar(@PathVariable Long id, @RequestBody AlumnoRequestDTO dto) {
        return alumnoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        alumnoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}