package com.academia.academiaerp.service;

import com.academia.academiaerp.dto.AlumnoInscritoDTO;
import com.academia.academiaerp.exception.RecursoNoEncontradoException;
import com.academia.academiaerp.model.Inscripcion;
import com.academia.academiaerp.repository.InscripcionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;

    public InscripcionService(InscripcionRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
    }

    // Contar alumnos activos en una categoría
    public long contarActivos(Long categoriaId) {
        return inscripcionRepository.countByCategoriaIdAndActivaTrue(categoriaId);
    }

    // Listar los alumnos inscritos (activos) de una categoría
    @Transactional(readOnly = true)
    public List<AlumnoInscritoDTO> listarInscritos(Long categoriaId) {
        return inscripcionRepository.findByCategoriaIdAndActivaTrue(categoriaId).stream()
                .map(ins -> new AlumnoInscritoDTO(
                        ins.getId(),
                        ins.getAlumno().getId(),
                        ins.getAlumno().getNombres() + " " + ins.getAlumno().getApellidos(),
                        ins.getAlumno().getDni()
                ))
                .toList();
    }

    // Desactivar una inscripción (quitar alumno de la categoría)
    public void desactivar(Long inscripcionId) {
        Inscripcion ins = inscripcionRepository.findById(inscripcionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Inscripción no encontrada"));
        ins.setActiva(false);
        inscripcionRepository.save(ins);
    }
}