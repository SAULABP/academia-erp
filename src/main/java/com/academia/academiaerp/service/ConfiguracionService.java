package com.academia.academiaerp.service;

import com.academia.academiaerp.model.Configuracion;
import com.academia.academiaerp.repository.ConfiguracionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ConfiguracionService {

    private final ConfiguracionRepository configuracionRepository;
    private final FileStorageService fileStorageService;

    public ConfiguracionService(ConfiguracionRepository configuracionRepository,
                                FileStorageService fileStorageService) {
        this.configuracionRepository = configuracionRepository;
        this.fileStorageService = fileStorageService;
    }

    // Obtener la config (o crear una por defecto si no existe)
    public Configuracion obtener() {
        return configuracionRepository.findById(1L)
                .orElseGet(() -> {
                    Configuracion nueva = new Configuracion();
                    nueva.setNombreClub("Mi Academia");
                    return configuracionRepository.save(nueva);
                });
    }

    // Actualizar los datos
    public Configuracion actualizar(Configuracion datos) {
        Configuracion config = obtener();
        config.setNombreClub(datos.getNombreClub());
        config.setDireccion(datos.getDireccion());
        config.setTelefono(datos.getTelefono());
        config.setRuc(datos.getRuc());
        return configuracionRepository.save(config);
    }

    // Subir/reemplazar el logo
    public Configuracion subirLogo(MultipartFile archivo) {
        Configuracion config = obtener();
        String rutaCompleta = fileStorageService.guardarEn(archivo, "logos");
        String soloNombre = rutaCompleta.substring(rutaCompleta.indexOf("/") + 1);
        config.setLogoUrl(soloNombre);
        return configuracionRepository.save(config);
    }

    public org.springframework.core.io.Resource cargarLogo(String nombreArchivo) {
        return fileStorageService.cargarRuta("logos/" + nombreArchivo);
    }
}