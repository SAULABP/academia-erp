package com.academia.academiaerp.repository;

import com.academia.academiaerp.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    List<Inscripcion> findByAlumnoId(Long alumnoId);

    List<Inscripcion> findByCategoriaId(Long categoriaId);

    boolean existsByAlumnoIdAndCategoriaId(Long alumnoId, Long categoriaId);
    boolean existsByAlumnoIdAndActivaTrue(Long alumnoId);
}