package com.academia.academiaerp.controller;

import com.academia.academiaerp.enums.CategoriaProducto;
import com.academia.academiaerp.model.Producto;
import com.academia.academiaerp.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> listar(@RequestParam(required = false) CategoriaProducto categoria) {
        return productoService.listar(categoria);
    }

    @GetMapping("/buscar")
    public List<Producto> buscar(@RequestParam String nombre) {
        return productoService.buscar(nombre);
    }

    @GetMapping("/{id}")
    public Producto obtener(@PathVariable Long id) {
        return productoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        return new ResponseEntity<>(productoService.crear(producto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @RequestBody Producto datos) {
        return productoService.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}