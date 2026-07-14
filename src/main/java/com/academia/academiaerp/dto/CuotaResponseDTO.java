    package com.academia.academiaerp.dto;

    import com.academia.academiaerp.enums.EstadoCuota;
    import lombok.Getter;
    import lombok.Setter;

    import java.math.BigDecimal;
    import java.time.LocalDate;

    @Getter
    @Setter
    public class CuotaResponseDTO {

        private Long id;
        private String periodo;
        private BigDecimal montoTotal;
        private BigDecimal montoPagado;
        private BigDecimal saldoPendiente;
        private LocalDate fechaVencimiento;
        private EstadoCuota estado;

        private Long alumnoId;
        private String alumnoNombreCompleto;
    }