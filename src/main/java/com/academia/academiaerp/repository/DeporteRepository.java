package com.academia.academiaerp.repository;

import com.academia.academiaerp.model.Deporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface DeporteRepository extends JpaRepository<Deporte, Long> {
}
