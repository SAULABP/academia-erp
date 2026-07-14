package com.academia.academiaerp.service;

import com.academia.academiaerp.exception.RecursoNoEncontradoException;
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
    // Guardar en una subcarpeta específica (ej: "productos")
    public String guardarEn(MultipartFile archivo, String subcarpeta) {
        if (archivo == null || archivo.isEmpty()) {
            throw new ReglaNegocioException("El archivo está vacío");
        }

        try {
            Path carpetaDestino = uploadPath.resolve(subcarpeta);
            Files.createDirectories(carpetaDestino);

            String extension = obtenerExtension(archivo.getOriginalFilename());
            String nombreUnico = UUID.randomUUID() + extension;

            Path destino = carpetaDestino.resolve(nombreUnico);
            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            // Devuelve la ruta relativa: "productos/uuid.jpg"
            return subcarpeta + "/" + nombreUnico;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo", e);
        }
    }

    // Cargar desde cualquier ruta relativa (incluyendo subcarpetas)
    public Resource cargarRuta(String rutaRelativa) {
        try {
            Path archivo = uploadPath.resolve(rutaRelativa);
            Resource recurso = new UrlResource(archivo.toUri());

            if (recurso.exists() && recurso.isReadable()) {
                return recurso;
            } else {
                throw new RecursoNoEncontradoException("No se encontró el archivo: " + rutaRelativa);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error al cargar el archivo: " + rutaRelativa, e);
        }
    }

    private String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null || !nombreArchivo.contains(".")) {
            return "";
        }
        return nombreArchivo.substring(nombreArchivo.lastIndexOf("."));
    }
}