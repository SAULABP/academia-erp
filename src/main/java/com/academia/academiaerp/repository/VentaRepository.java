package com.academia.academiaerp.repository;

import com.academia.academiaerp.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findBySedeId(Long sedeId);
    List<Venta> findByFechaBetween(java.time.LocalDateTime desde, java.time.LocalDateTime hasta);
}