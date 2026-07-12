package com.academia.academiaerp.repository;

import com.academia.academiaerp.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    List<Alumno> findBySedeId(Long sedeId);

    List<Alumno> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(
            String nombres, String apellidos);

    boolean existsByDni(String dni);
}