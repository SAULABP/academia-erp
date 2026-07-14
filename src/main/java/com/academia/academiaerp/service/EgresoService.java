package com.academia.academiaerp.service;

import com.academia.academiaerp.model.Egreso;
import com.academia.academiaerp.repository.EgresoRepository;
import com.academia.academiaerp.exception.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EgresoService {

    private final EgresoRepository egresoRepository;

    public EgresoService(EgresoRepository egresoRepository) {
        this.egresoRepository = egresoRepository;
    }

    public List<Egreso> listar() {
        return egresoRepository.findAll();
    }

    public Egreso crear(Egreso egreso) {
        return egresoRepository.save(egreso);
    }

    public Egreso actualizar(Long id, Egreso datos) {
        Egreso egreso = egresoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Egreso no encontrado"));
        egreso.setConcepto(datos.getConcepto());
        egreso.setMonto(datos.getMonto());
        egreso.setFecha(datos.getFecha());
        egreso.setCategoria(datos.getCategoria());
        egreso.setSede(datos.getSede());
        return egresoRepository.save(egreso);
    }

    public void eliminar(Long id) {
        egresoRepository.deleteById(id);
    }
}