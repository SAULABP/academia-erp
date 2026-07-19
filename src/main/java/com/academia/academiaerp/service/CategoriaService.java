package com.academia.academiaerp.service;

import com.academia.academiaerp.dto.CategoriaConConteoDTO;
import com.academia.academiaerp.exception.RecursoNoEncontradoException;
import com.academia.academiaerp.model.Categoria;
import com.academia.academiaerp.model.Deporte;
import com.academia.academiaerp.model.Sede;
import com.academia.academiaerp.repository.CategoriaRepository;
import com.academia.academiaerp.repository.InscripcionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final SedeService sedeService;
    private final DeporteService deporteService;
    private final InscripcionRepository inscripcionRepository;

    public CategoriaService(CategoriaRepository categoriaRepository,
                            SedeService sedeService,
                            DeporteService deporteService,
                            InscripcionRepository inscripcionRepository) {
        this.categoriaRepository = categoriaRepository;
        this.sedeService = sedeService;
        this.deporteService = deporteService;
        this.inscripcionRepository = inscripcionRepository;
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public List<Categoria> listarPorSede(Long sedeId) {
        return categoriaRepository.findBySedeId(sedeId);
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoria  no encontrada con id: " + id));

    }

    public Categoria crear(Categoria categoria) {
        Sede sede = sedeService.buscarPorId(categoria.getSede().getId());
        Deporte deporte = deporteService.buscarPorId(categoria.getDeporte().getId());

        categoria.setSede(sede);
        categoria.setDeporte(deporte);

        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Long id, Categoria datos) {
        Categoria categoria = buscarPorId(id);

        categoria.setNombre(datos.getNombre());
        categoria.setEdadMinima(datos.getEdadMinima());
        categoria.setEdadMaxima(datos.getEdadMaxima());
        categoria.setCupoMaximo(datos.getCupoMaximo());   // <-- nuevo
        categoria.setHorario(datos.getHorario());

        Sede sede = sedeService.buscarPorId(datos.getSede().getId());
        Deporte deporte = deporteService.buscarPorId(datos.getDeporte().getId());
        categoria.setSede(sede);
        categoria.setDeporte(deporte);

        return categoriaRepository.save(categoria);
    }

    public void eliminar(Long id) {
        Categoria categoria = buscarPorId(id);
        categoriaRepository.delete(categoria);
    }
    @Transactional(readOnly = true)
    public List<CategoriaConConteoDTO> listarConConteo() {
        return categoriaRepository.findAll().stream()
                .map(cat -> new CategoriaConConteoDTO(
                        cat.getId(),
                        cat.getNombre(),
                        cat.getEdadMinima(),
                        cat.getEdadMaxima(),
                        cat.getCupoMaximo(),
                        cat.getHorario(),
                        cat.getDeporte().getNombre(),
                        cat.getSede().getNombre(),
                        inscripcionRepository.countByCategoriaIdAndActivaTrue(cat.getId())
                ))
                .toList();
    }
}