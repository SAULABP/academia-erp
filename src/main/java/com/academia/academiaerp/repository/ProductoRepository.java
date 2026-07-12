package com.academia.academiaerp.repository;

import com.academia.academiaerp.enums.CategoriaProducto;
import com.academia.academiaerp.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoria(CategoriaProducto categoria);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}