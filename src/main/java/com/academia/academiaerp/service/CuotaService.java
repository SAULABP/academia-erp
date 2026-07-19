package com.academia.academiaerp.service;

import com.academia.academiaerp.dto.CuotaResponseDTO;
import com.academia.academiaerp.enums.EstadoCuota;
import com.academia.academiaerp.enums.TipoCuota;
import com.academia.academiaerp.exception.RecursoNoEncontradoException;
import com.academia.academiaerp.exception.ReglaNegocioException;
import com.academia.academiaerp.model.Alumno;
import com.academia.academiaerp.model.Cuota;
import com.academia.academiaerp.repository.AlumnoRepository;
import com.academia.academiaerp.repository.CuotaRepository;
import com.academia.academiaerp.repository.InscripcionRepository;
import com.academia.academiaerp.repository.PagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.academia.academiaerp.dto.PagoRequestDTO;
import com.academia.academiaerp.model.Pago;
import java.time.LocalDateTime;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class CuotaService {

    private final CuotaRepository cuotaRepository;
    private final AlumnoRepository alumnoRepository;
    private final PagoRepository pagoRepository;
    private final InscripcionRepository inscripcionRepository;

    public CuotaService(CuotaRepository cuotaRepository,
                        AlumnoRepository alumnoRepository,
                        PagoRepository pagoRepository,
                        InscripcionRepository inscripcionRepository) {
        this.cuotaRepository = cuotaRepository;
        this.alumnoRepository = alumnoRepository;
        this.pagoRepository = pagoRepository;
        this.inscripcionRepository = inscripcionRepository;
    }

    // ---- Traductor: entidad Cuota -> DTO ----
    private CuotaResponseDTO convertirAResponse(Cuota cuota) {
        CuotaResponseDTO dto = new CuotaResponseDTO();
        dto.setId(cuota.getId());
        dto.setPeriodo(cuota.getPeriodo());
        dto.setMontoTotal(cuota.getMontoTotal());
        dto.setMontoPagado(cuota.getMontoPagado());
        dto.setSaldoPendiente(cuota.getMontoTotal().subtract(cuota.getMontoPagado()));
        dto.setFechaVencimiento(cuota.getFechaVencimiento());
        dto.setEstado(cuota.getEstado());
        Alumno alumno = cuota.getAlumno();
        dto.setAlumnoId(alumno.getId());
        dto.setAlumnoNombreCompleto(alumno.getNombres() + " " + alumno.getApellidos());
        dto.setApoderadoNombre(cuota.getAlumno().getApoderadoNombre());
        dto.setApoderadoTelefono(cuota.getAlumno().getApoderadoTelefono());
        dto.setTipo(cuota.getTipo());

        return dto;
    }

    // ---- Generar una cuota para UN alumno ----
    @Transactional
    public CuotaResponseDTO generarCuota(Long alumnoId, String periodo, LocalDate fechaVencimiento) {
        Alumno alumno = alumnoRepository.findById(alumnoId)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Alumno no encontrado con id: " + alumnoId));

            if (cuotaRepository.existsByAlumnoIdAndPeriodo(alumnoId, periodo)) {
                throw new ReglaNegocioException("El alumno ya tiene una cuota para el periodo " + periodo);
            }

        Cuota cuota = new Cuota();
        cuota.setAlumno(alumno);
        cuota.setPeriodo(periodo);
        cuota.setMontoTotal(alumno.getCuotaMensual());
        cuota.setMontoPagado(BigDecimal.ZERO);
        cuota.setFechaVencimiento(fechaVencimiento);
        cuota.setEstado(EstadoCuota.PENDIENTE);
        cuota.setTipo(TipoCuota.MENSUALIDAD);

        Cuota guardada = cuotaRepository.save(cuota);
        return convertirAResponse(guardada);
    }

    // ---- Generar cuotas para TODOS los alumnos (botón masivo) ----
    @Transactional
    public List<CuotaResponseDTO> generarCuotasMasivas(String periodo) {
        List<Alumno> alumnos = alumnoRepository.findAll();

        YearMonth ym = YearMonth.parse(periodo);
        LocalDate finDelPeriodo = ym.atEndOfMonth();

        return alumnos.stream()
                .filter(alumno -> !cuotaRepository.existsByAlumnoIdAndPeriodo(alumno.getId(), periodo))
                .filter(alumno -> alumno.getFechaRegistro() == null
                        || !alumno.getFechaRegistro().isAfter(finDelPeriodo))
                .filter(alumno -> inscripcionRepository.existsByAlumnoIdAndActivaTrue(alumno.getId()))
                .map(alumno -> {
                    Cuota cuota = new Cuota();
                    cuota.setAlumno(alumno);
                    cuota.setPeriodo(periodo);
                    cuota.setMontoTotal(alumno.getCuotaMensual());
                    cuota.setMontoPagado(BigDecimal.ZERO);
                    cuota.setFechaVencimiento(calcularVencimiento(alumno, ym));
                    cuota.setEstado(EstadoCuota.PENDIENTE);
                    cuota.setTipo(TipoCuota.MENSUALIDAD);
                    return convertirAResponse(cuotaRepository.save(cuota));
                })
                .toList();
    }

    // Calcula el vencimiento: el día de inicio del alumno, en el mes del periodo
    private LocalDate calcularVencimiento(Alumno alumno, YearMonth periodo) {
        LocalDate inicio = alumno.getFechaInicio() != null
                ? alumno.getFechaInicio()
                : alumno.getFechaRegistro();

        // Si no hay ninguna fecha, usar fin de mes como respaldo
        if (inicio == null) {
            return periodo.atEndOfMonth();
        }

        int diaPago = inicio.getDayOfMonth();
        // Si el día no existe en el mes (ej. 31 en febrero), usar el último día
        int ultimoDia = periodo.lengthOfMonth();
        int dia = Math.min(diaPago, ultimoDia);

        return periodo.atDay(dia);
    }

    // ---- Registrar un pago sobre una cuota ----
    @Transactional
    public CuotaResponseDTO registrarPago(PagoRequestDTO dto) {
        // 1. Buscar la cuota
        Cuota cuota = cuotaRepository.findById(dto.getCuotaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuota no encontrada con id: " + dto.getCuotaId()));

        // 2. Validar que la cuota no esté ya pagada
        if (cuota.getEstado() == EstadoCuota.PAGADA) {
            throw new ReglaNegocioException("La cuota ya está pagada");
        }

        // 3. Validar el monto del pago
        if (dto.getMonto() == null || dto.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("El monto del pago debe ser mayor a cero");
        }

        BigDecimal saldoPendiente = cuota.getMontoTotal().subtract(cuota.getMontoPagado());
        if (dto.getMonto().compareTo(saldoPendiente) > 0) {
            throw new ReglaNegocioException("El pago (" + dto.getMonto() +
                    ") supera el saldo pendiente (" + saldoPendiente + ")");
        }

        // 4. Registrar el pago
        Pago pago = new Pago();
        pago.setCuota(cuota);
        pago.setMonto(dto.getMonto());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setFechaPago(LocalDateTime.now());
        pagoRepository.save(pago);

        // 5. Actualizar el montoPagado de la cuota
        BigDecimal nuevoMontoPagado = cuota.getMontoPagado().add(dto.getMonto());
        cuota.setMontoPagado(nuevoMontoPagado);

        // 6. Recalcular el estado
        if (nuevoMontoPagado.compareTo(cuota.getMontoTotal()) >= 0) {
            cuota.setEstado(EstadoCuota.PAGADA);
        } else {
            cuota.setEstado(EstadoCuota.PARCIAL);
        }

        Cuota actualizada = cuotaRepository.save(cuota);
        return convertirAResponse(actualizada);
    }

    public List<CuotaResponseDTO> listar(Long alumnoId, EstadoCuota estado) {
        List<Cuota> cuotas;
        if (alumnoId != null) {
            cuotas = cuotaRepository.findByAlumnoId(alumnoId);
        } else if (estado != null) {
            cuotas = cuotaRepository.findByEstado(estado);
        } else {
            cuotas = cuotaRepository.findAll();
        }
        return cuotas.stream().map(this::convertirAResponse).toList();
    }
}