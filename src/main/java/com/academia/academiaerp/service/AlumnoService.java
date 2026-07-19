package com.academia.academiaerp.service;

import com.academia.academiaerp.dto.AlumnoRequestDTO;
import com.academia.academiaerp.dto.AlumnoResponseDTO;
import com.academia.academiaerp.dto.CategoriaResumenDTO;
import com.academia.academiaerp.enums.EstadoCuota;
import com.academia.academiaerp.enums.TipoCuota;
import com.academia.academiaerp.exception.RecursoNoEncontradoException;
import com.academia.academiaerp.exception.ReglaNegocioException;
import com.academia.academiaerp.model.*;
import com.academia.academiaerp.repository.AlumnoRepository;
import com.academia.academiaerp.repository.CuotaRepository;
import com.academia.academiaerp.repository.InscripcionRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final InscripcionRepository inscripcionRepository;
    private final SedeService sedeService;
    private final CategoriaService categoriaService;
    private final FileStorageService fileStorageService;
    private final CuotaRepository  cuotaRepository;

    public AlumnoService(AlumnoRepository alumnoRepository,
                         InscripcionRepository inscripcionRepository,
                         SedeService sedeService,
                         CategoriaService categoriaService,
                         FileStorageService fileStorageService,
                         CuotaRepository cuotaRepository) {
        this.alumnoRepository = alumnoRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.sedeService = sedeService;
        this.categoriaService = categoriaService;
        this.fileStorageService = fileStorageService;
        this.cuotaRepository = cuotaRepository;
    }

    // ---- Traductor: entidad Alumno -> AlumnoResponseDTO ----
    private AlumnoResponseDTO convertirAResponse(Alumno alumno) {
        AlumnoResponseDTO dto = new AlumnoResponseDTO();
        dto.setId(alumno.getId());
        dto.setNombres(alumno.getNombres());
        dto.setApellidos(alumno.getApellidos());
        dto.setDni(alumno.getDni());
        dto.setEdad(alumno.getEdad());
        dto.setTelefono(alumno.getTelefono());
        dto.setFotoUrl(alumno.getFotoUrl());
        dto.setCuotaMensual(alumno.getCuotaMensual());
        dto.setApoderadoNombre(alumno.getApoderadoNombre());
        dto.setApoderadoTelefono(alumno.getApoderadoTelefono());
        dto.setSedeId(alumno.getSede().getId());
        dto.setSedeNombre(alumno.getSede().getNombre());

        List<Inscripcion> inscripciones = inscripcionRepository.findByAlumnoId(alumno.getId());
        List<CategoriaResumenDTO> categorias = inscripciones.stream()
                .map(ins -> new CategoriaResumenDTO(
                        ins.getCategoria().getId(),
                        ins.getCategoria().getNombre()))
                .toList();
        dto.setCategorias(categorias);

        return dto;
    }

    // ---- Crear alumno + sus inscripciones ----
    @Transactional
    public AlumnoResponseDTO crear(AlumnoRequestDTO dto)    {
        // 1. Validar que el DNI no exista
        if (alumnoRepository.existsByDni(dto.getDni())) {
            throw new ReglaNegocioException("Ya existe un alumno con DNI: " + dto.getDni());
        }

        // 2. Buscar la sede real (valida que exista)
        Sede sede = sedeService.buscarPorId(dto.getSedeId());

        // 3. Construir la entidad Alumno desde el DTO
        Alumno alumno = new Alumno();
        alumno.setNombres(dto.getNombres());
        alumno.setApellidos(dto.getApellidos());
        alumno.setDni(dto.getDni());
        alumno.setEdad(dto.getEdad());
        alumno.setTelefono(dto.getTelefono());
        alumno.setFotoUrl(dto.getFotoUrl());
        alumno.setCuotaMensual(dto.getCuotaMensual());
        alumno.setApoderadoNombre(dto.getApoderadoNombre());
        alumno.setApoderadoTelefono(dto.getApoderadoTelefono());
        alumno.setSede(sede);
        alumno.setFechaInicio(dto.getFechaInicio());

        // 4. Guardar el alumno (ya tiene id)
        Alumno alumnoGuardado = alumnoRepository.save(alumno);
        if (dto.getMontoMatricula() != null
                && dto.getMontoMatricula().compareTo(BigDecimal.ZERO) > 0) {
            Cuota matricula = new Cuota();
            matricula.setAlumno(alumnoGuardado);
            matricula.setTipo(TipoCuota.MATRICULA);
            matricula.setPeriodo(YearMonth.now().toString());
            matricula.setMontoTotal(dto.getMontoMatricula());
            matricula.setMontoPagado(BigDecimal.ZERO);
            matricula.setFechaVencimiento(LocalDate.now().plusDays(7));
            matricula.setEstado(EstadoCuota.PENDIENTE);
            cuotaRepository.save(matricula);
        }

        // 5. Crear una inscripción por cada categoriaId recibido
        if (dto.getCategoriaIds() != null) {
            for (Long categoriaId : dto.getCategoriaIds()) {
                Categoria categoria = categoriaService.buscarPorId(categoriaId);

                Inscripcion inscripcion = new Inscripcion();
                inscripcion.setAlumno(alumnoGuardado);
                inscripcion.setCategoria(categoria);
                inscripcion.setFechaInscripcion(LocalDate.now());
                inscripcion.setActiva(true);

                inscripcionRepository.save(inscripcion);
            }
        }

        // 6. Devolver el DTO limpio
        return convertirAResponse(alumnoGuardado);
    }
    // ---- Listar todos (o por sede si se pasa sedeId) ----
    public List<AlumnoResponseDTO> listar(Long sedeId) {
        List<Alumno> alumnos;
        if (sedeId != null) {
            alumnos = alumnoRepository.findBySedeId(sedeId);
        } else {
            alumnos = alumnoRepository.findAll();
        }
        return alumnos.stream()
                .map(this::convertirAResponse)
                .toList();
    }

    // ---- Buscar por nombre o apellido ----
    public List<AlumnoResponseDTO> buscarPorTexto(String texto) {
        return alumnoRepository
                .findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(texto, texto)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    // ---- Buscar uno por id ----
    public AlumnoResponseDTO buscarPorId(Long id) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Alumno no encontrada con id: " + id));

        return convertirAResponse(alumno);
    }

    // ---- Actualizar datos del alumno ----
    @Transactional
    public AlumnoResponseDTO actualizar(Long id, AlumnoRequestDTO dto) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Alumno no encontrado con id: " + id));

        // Si el DNI cambió, validar que el nuevo no lo tenga otro alumno
        if (!alumno.getDni().equals(dto.getDni())
                && alumnoRepository.existsByDni(dto.getDni())) {
            throw new ReglaNegocioException("Ya existe otro alumno con el DNI: " + dto.getDni());
        }

        alumno.setDni(dto.getDni());
        alumno.setNombres(dto.getNombres());
        alumno.setApellidos(dto.getApellidos());
        alumno.setEdad(dto.getEdad());
        alumno.setTelefono(dto.getTelefono());
        alumno.setFotoUrl(dto.getFotoUrl());
        alumno.setCuotaMensual(dto.getCuotaMensual());
        alumno.setApoderadoNombre(dto.getApoderadoNombre());
        alumno.setApoderadoTelefono(dto.getApoderadoTelefono());
        alumno.setFechaInicio(dto.getFechaInicio());

        Sede sede = sedeService.buscarPorId(dto.getSedeId());
        alumno.setSede(sede);

        Alumno actualizado = alumnoRepository.save(alumno);
        return convertirAResponse(actualizado);
    }

    public Resource cargarFoto(String nombreArchivo) {
        return fileStorageService.cargar(nombreArchivo);
    }

    // ---- Eliminar ----
    @Transactional
    public void eliminar(Long id) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Alumno no encontrado con id: " + id));
        inscripcionRepository.deleteAll(inscripcionRepository.findByAlumnoId(id));
        alumnoRepository.delete(alumno);
    }
    @Transactional
    public AlumnoResponseDTO subirFoto(Long alumnoId, MultipartFile archivo) {
        Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Alumno no encontrado con id: " + alumnoId));

        String nombreArchivo = fileStorageService.guardar(archivo);
        alumno.setFotoUrl(nombreArchivo);

        Alumno actualizado = alumnoRepository.save(alumno);
        return convertirAResponse(actualizado);
    }
}
