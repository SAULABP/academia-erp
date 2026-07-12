package com.academia.academiaerp.controller;

import com.academia.academiaerp.model.Categoria;
import com.academia.academiaerp.service.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<Categoria> listar(@RequestParam(required = false) Long sedeId) {
        if (sedeId != null) {
            return categoriaService.listarPorSede(sedeId);
        }
        return categoriaService.listarTodas();
    }

    @GetMapping("/{id}")
    public Categoria obtener(@PathVariable Long id) {
        return categoriaService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Categoria> crear(@RequestBody Categoria categoria) {
        Categoria creada = categoriaService.crear(categoria);
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Categoria actualizar(@PathVariable Long id, @RequestBody Categoria datos) {
        return categoriaService.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}