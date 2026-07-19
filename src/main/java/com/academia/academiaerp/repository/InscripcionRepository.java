package com.academia.academiaerp.repository;

import com.academia.academiaerp.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    List<Inscripcion> findByAlumnoId(Long alumnoId);

    List<Inscripcion> findByCategoriaId(Long categoriaId);

    boolean existsByAlumnoIdAndCategoriaId(Long alumnoId, Long categoriaId);
    boolean existsByAlumnoIdAndActivaTrue(Long alumnoId);
    // Contar alumnos activos en una categoría (para la card "15/25")
    long countByCategoriaIdAndActivaTrue(Long categoriaId);

    // Listar inscripciones activas de una categoría (para ver los inscritos)
    List<Inscripcion> findByCategoriaIdAndActivaTrue(Long categoriaId);
}