package com.academia.academiaerp.service;

import com.academia.academiaerp.exception.ReglaNegocioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.net.MalformedURLException;

@Service
public class FileStorageService {

    private final Path uploadPath;

    public FileStorageService(@Value("${app.upload-dir}") String uploadDir) {
        this.uploadPath = Paths.get(uploadDir);
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear la carpeta de subida", e);
        }
    }

    public String guardar(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new ReglaNegocioException("El archivo está vacío");

        }

        String extension = obtenerExtension(archivo.getOriginalFilename());
        String nombreUnico = UUID.randomUUID() + extension;

        try {
            Path destino = uploadPath.resolve(nombreUnico);
            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
            return nombreUnico;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo", e);
        }
    }

    public Resource cargar(String nombreArchivo) {
        try {
            Path archivo = uploadPath.resolve(nombreArchivo);
            Resource recurso = new UrlResource(archivo.toUri());

            if (recurso.exists() && recurso.isReadable()) {
                return recurso;
            } else {
                throw new ReglaNegocioException("No se pudo leer el archivo: " + nombreArchivo);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error al cargar el archivo: " + nombreArchivo, e);
        }
    }

    private String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null || !nombreArchivo.contains(".")) {
            return "";
        }
        return nombreArchivo.substring(nombreArchivo.lastIndexOf("."));
    }
}