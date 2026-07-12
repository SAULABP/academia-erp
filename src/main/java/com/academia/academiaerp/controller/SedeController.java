package com.academia.academiaerp.controller;

import com.academia.academiaerp.model.Sede;
import com.academia.academiaerp.service.SedeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sedes")
public class SedeController {

    private final SedeService sedeService;

    public SedeController(SedeService sedeService) {
        this.sedeService = sedeService;
    }

    @GetMapping
    public List<Sede> listar() {
        return sedeService.listarTodas();
    }

    @GetMapping("/{id}")
    public Sede obtener(@PathVariable Long id) {
        return sedeService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Sede> crear(@RequestBody Sede sede) {
        Sede creada = sedeService.crear(sede);
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Sede actualizar(@PathVariable Long id, @RequestBody Sede datos) {
        return sedeService.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        sedeService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}