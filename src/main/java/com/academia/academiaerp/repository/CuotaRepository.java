package com.academia.academiaerp.repository;

import com.academia.academiaerp.enums.EstadoCuota;
import com.academia.academiaerp.model.Cuota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuotaRepository extends JpaRepository<Cuota, Long> {

    List<Cuota> findByAlumnoId(Long alumnoId);

    List<Cuota> findByEstado(EstadoCuota estado);

    boolean existsByAlumnoIdAndPeriodo(Long alumnoId, String periodo);
}