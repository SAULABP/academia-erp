package com.academia.academiaerp.controller;

import com.academia.academiaerp.model.Egreso;
import com.academia.academiaerp.service.EgresoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/v1/egresos")
public class EgresoController {

    private final EgresoService egresoService;

    public EgresoController(EgresoService egresoService) {
        this.egresoService = egresoService;
    }

    @GetMapping
    public List<Egreso> listar() {
        return egresoService.listar();
    }

    @PostMapping
    public ResponseEntity<Egreso> crear(@RequestBody Egreso egreso) {
        return new ResponseEntity<>(egresoService.crear(egreso), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Egreso actualizar(@PathVariable Long id, @RequestBody Egreso egreso) {
        return egresoService.actualizar(id, egreso);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        egresoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}