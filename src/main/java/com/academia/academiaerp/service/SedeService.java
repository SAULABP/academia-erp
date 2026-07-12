package com.academia.academiaerp.service;

import com.academia.academiaerp.exception.RecursoNoEncontradoException;
import com.academia.academiaerp.model.Sede;
import com.academia.academiaerp.repository.SedeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SedeService {

    private final SedeRepository sedeRepository;

    public SedeService(SedeRepository sedeRepository) {
        this.sedeRepository = sedeRepository;
    }

    public List<Sede> listarTodas() {
        return sedeRepository.findAll();
    }

    public Sede buscarPorId(Long id) {
        return sedeRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Sede no encontrada con id: " + id));

    }

    public Sede crear(Sede sede) {
        return sedeRepository.save(sede);
    }

    public Sede actualizar(Long id, Sede datos) {
        Sede sede = buscarPorId(id);
        sede.setNombre(datos.getNombre());
        sede.setDireccion(datos.getDireccion());
        sede.setTelefono(datos.getTelefono());
        return sedeRepository.save(sede);
    }

    public void eliminar(Long id) {
        Sede sede = buscarPorId(id);
        sedeRepository.delete(sede);
    }
}