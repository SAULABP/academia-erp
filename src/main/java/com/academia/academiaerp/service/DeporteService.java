package com.academia.academiaerp.service;

import com.academia.academiaerp.exception.RecursoNoEncontradoException;
import com.academia.academiaerp.model.Deporte;
import com.academia.academiaerp.repository.DeporteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeporteService {

    private final DeporteRepository deporteRepository;

    public DeporteService(DeporteRepository deporteRepository) {
        this.deporteRepository = deporteRepository;
    }

    public List<Deporte> listar() {
        return deporteRepository.findAll();
    }


    public Deporte buscarPorId(Long id) {
        return deporteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Deporte no encontrado con id: " + id));

    }

    public Deporte crear(Deporte deporte) {
        return deporteRepository.save(deporte);
    }


    public Deporte actualizar(Long id, Deporte deporteActualizado) {

        Deporte deporte = buscarPorId(id);
        deporte.setNombre(deporteActualizado.getNombre());
        return deporteRepository.save(deporte);
    }

    public void eliminar(Long id) {

        Deporte deporte = buscarPorId(id);
        deporteRepository.delete(deporte);
    }

}
