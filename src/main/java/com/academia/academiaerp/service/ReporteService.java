package com.academia.academiaerp.service;

import com.academia.academiaerp.dto.ReporteResumenDTO;
import com.academia.academiaerp.dto.EgresoPorCategoriaDTO;
import com.academia.academiaerp.enums.CategoriaEgreso;
import com.academia.academiaerp.model.Egreso;
import com.academia.academiaerp.model.Pago;
import com.academia.academiaerp.model.Venta;
import com.academia.academiaerp.repository.EgresoRepository;
import com.academia.academiaerp.repository.PagoRepository;
import com.academia.academiaerp.repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReporteService {

    private final PagoRepository pagoRepository;
    private final VentaRepository ventaRepository;
    private final EgresoRepository egresoRepository;

    public ReporteService(PagoRepository pagoRepository,
                          VentaRepository ventaRepository,
                          EgresoRepository egresoRepository) {
        this.pagoRepository = pagoRepository;
        this.ventaRepository = ventaRepository;
        this.egresoRepository = egresoRepository;
    }

    public ReporteResumenDTO generarReporte(String periodo) {
        // periodo formato "2026-07" -> calcular rango del mes
        YearMonth ym = YearMonth.parse(periodo);
        LocalDate primerDia = ym.atDay(1);
        LocalDate ultimoDia = ym.atEndOfMonth();
        LocalDateTime inicio = primerDia.atStartOfDay();
        LocalDateTime fin = ultimoDia.atTime(23, 59, 59);

        // 1. Ingresos por cuotas (pagos del periodo)
        List<Pago> pagos = pagoRepository.findByFechaPagoBetween(inicio, fin);
        BigDecimal ingresosCuotas = pagos.stream()
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2. Ingresos por ventas del periodo
        List<Venta> ventas = ventaRepository.findByFechaBetween(inicio, fin);
        BigDecimal ingresosVentas = ventas.stream()
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalIngresos = ingresosCuotas.add(ingresosVentas);

        // 3. Egresos del periodo
        List<Egreso> egresos = egresoRepository.findByFechaBetween(primerDia, ultimoDia);
        BigDecimal totalEgresos = egresos.stream()
                .map(Egreso::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. Balance
        BigDecimal balance = totalIngresos.subtract(totalEgresos);

        // 5. Egresos agrupados por categoría
        List<EgresoPorCategoriaDTO> porCategoria = new ArrayList<>();
        for (CategoriaEgreso cat : CategoriaEgreso.values()) {
            BigDecimal totalCat = egresos.stream()
                    .filter(e -> e.getCategoria() == cat)
                    .map(Egreso::getMonto)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (totalCat.compareTo(BigDecimal.ZERO) > 0) {
                porCategoria.add(new EgresoPorCategoriaDTO(cat, totalCat));
            }
        }

        return new ReporteResumenDTO(periodo, ingresosCuotas, ingresosVentas,
                totalIngresos, totalEgresos, balance, porCategoria);
    }
}