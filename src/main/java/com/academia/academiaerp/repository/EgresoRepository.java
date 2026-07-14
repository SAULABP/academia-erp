package com.academia.academiaerp.repository;

import com.academia.academiaerp.model.Egreso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface EgresoRepository extends JpaRepository<Egreso, Long> {

    // Egresos entre dos fechas (para el reporte)
    List<Egreso> findByFechaBetween(LocalDate desde, LocalDate hasta);
}