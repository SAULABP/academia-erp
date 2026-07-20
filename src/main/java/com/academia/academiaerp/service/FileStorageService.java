package com.academia.academiaerp.service;

import com.academia.academiaerp.exception.ReglaNegocioException;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileStorageService {

    private final Cloudinary cloudinary;

    public FileStorageService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    // Sube a Cloudinary (carpeta general) y devuelve la URL completa
    public String guardar(MultipartFile archivo) {
        return subirACloudinary(archivo, "academia");
    }

    // Sube a una subcarpeta específica (productos, logos) y devuelve la URL
    public String guardarEn(MultipartFile archivo, String subcarpeta) {
        return subirACloudinary(archivo, "academia/" + subcarpeta);
    }

    // Lógica central de subida
    private String subirACloudinary(MultipartFile archivo, String carpeta) {
        if (archivo == null || archivo.isEmpty()) {
            throw new ReglaNegocioException("El archivo está vacío");
        }
        try {
            Map<?, ?> resultado = cloudinary.uploader().upload(
                    archivo.getBytes(),
                    ObjectUtils.asMap(
                            "folder", carpeta,
                            "resource_type", "image"
                    )
            );
            // La URL segura (https) de la imagen subida
            return resultado.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Error al subir la imagen a Cloudinary", e);
        }
    }
}