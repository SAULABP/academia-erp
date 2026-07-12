package com.academia.academiaerp.controller;

import com.academia.academiaerp.model.Deporte;
import com.academia.academiaerp.service.DeporteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deportes")
public class DeporteController {
    private final DeporteService deporteService;

    public DeporteController(DeporteService deporteService) {
        this.deporteService = deporteService;
    }
    @GetMapping
    public List<Deporte> listarAllDeportes() {
        return  deporteService.listar();
    }

    @GetMapping("/{id}")
    public Deporte obtener(@PathVariable Long id) {
        return deporteService.buscarPorId(id);
    }
    @PostMapping
    public ResponseEntity<Deporte> crear(@RequestBody Deporte deporte) {
        Deporte creado = deporteService.crear(deporte);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public Deporte actualizar(@PathVariable Long id, @RequestBody Deporte deporte) {
        return deporteService.actualizar(id, deporte);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        deporteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
