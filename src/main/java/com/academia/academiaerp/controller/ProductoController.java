package com.academia.academiaerp.controller;

import com.academia.academiaerp.enums.CategoriaProducto;
import com.academia.academiaerp.model.Producto;
import com.academia.academiaerp.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

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
    @PostMapping("/{id}/imagen")
    public Producto subirImagen(
            @PathVariable Long id,
            @RequestParam("archivo") MultipartFile archivo) {
        return productoService.subirImagen(id, archivo);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}