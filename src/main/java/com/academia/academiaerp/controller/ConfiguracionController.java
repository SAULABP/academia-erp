package com.academia.academiaerp.controller;

import com.academia.academiaerp.model.Configuracion;
import com.academia.academiaerp.service.ConfiguracionService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/configuracion")
public class ConfiguracionController {

    private final ConfiguracionService configuracionService;

    public ConfiguracionController(ConfiguracionService configuracionService) {
        this.configuracionService = configuracionService;
    }

    @GetMapping
    public Configuracion obtener() {
        return configuracionService.obtener();
    }

    @PutMapping
    public Configuracion actualizar(@RequestBody Configuracion datos) {
        return configuracionService.actualizar(datos);
    }

    @PostMapping("/logo")
    public Configuracion subirLogo(@RequestParam("archivo") MultipartFile archivo) {
        return configuracionService.subirLogo(archivo);
    }

    @GetMapping("/logo/{nombreArchivo}")
    public ResponseEntity<Resource> obtenerLogo(@PathVariable String nombreArchivo) {
        Resource recurso = configuracionService.cargarLogo(nombreArchivo);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(recurso);
    }
}