package com.academia.academiaerp.service;

import com.academia.academiaerp.dto.*;
import com.academia.academiaerp.exception.RecursoNoEncontradoException;
import com.academia.academiaerp.exception.ReglaNegocioException;
import com.academia.academiaerp.model.*;
import com.academia.academiaerp.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoService productoService;
    private final SedeService sedeService;

    public VentaService(VentaRepository ventaRepository,
                        ProductoService productoService,
                        SedeService sedeService) {
        this.ventaRepository = ventaRepository;
        this.productoService = productoService;
        this.sedeService = sedeService;
    }

    // ---- Traductor: entidad Venta -> DTO ----
    private VentaResponseDTO convertirAResponse(Venta venta) {
        VentaResponseDTO dto = new VentaResponseDTO();
        dto.setId(venta.getId());
        dto.setFecha(venta.getFecha());
        dto.setTotal(venta.getTotal());
        dto.setMetodoPago(venta.getMetodoPago());
        dto.setSedeId(venta.getSede().getId());
        dto.setSedeNombre(venta.getSede().getNombre());

        List<DetalleVentaResponseDTO> detallesDTO = venta.getDetalles().stream()
                .map(det -> {
                    DetalleVentaResponseDTO d = new DetalleVentaResponseDTO();
                    d.setProductoId(det.getProducto().getId());
                    d.setProductoNombre(det.getProducto().getNombre());
                    d.setCantidad(det.getCantidad());
                    d.setPrecioUnitario(det.getPrecioUnitario());
                    d.setSubtotal(det.getSubtotal());
                    return d;
                })
                .toList();
        dto.setDetalles(detallesDTO);

        return dto;
    }

    @Transactional
    public VentaResponseDTO procesarVenta(VentaRequestDTO dto) {
        // 1. Validar que haya items
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new ReglaNegocioException("La venta debe tener al menos un producto");
        }

        // 2. Buscar la sede (valida que exista)
        Sede sede = sedeService.buscarPorId(dto.getSedeId());

        // 3. Crear la venta (cabecera)
        Venta venta = new Venta();
        venta.setFecha(LocalDateTime.now());
        venta.setMetodoPago(dto.getMetodoPago());
        venta.setSede(sede);

        BigDecimal total = BigDecimal.ZERO;

        // 4. Procesar cada item del carrito
        for (DetalleVentaRequestDTO item : dto.getItems()) {
            Producto producto = productoService.buscarPorId(item.getProductoId());

            // 4a. Validar cantidad
            if (item.getCantidad() == null || item.getCantidad() <= 0) {
                throw new ReglaNegocioException("La cantidad debe ser mayor a cero para el producto: "
                        + producto.getNombre());
            }

            // 4b. Validar stock suficiente
            if (producto.getStock() < item.getCantidad()) {
                throw new ReglaNegocioException("Stock insuficiente para " + producto.getNombre()
                        + " (disponible: " + producto.getStock()
                        + ", solicitado: " + item.getCantidad() + ")");
            }

            // 4c. Descontar stock
            producto.setStock(producto.getStock() - item.getCantidad());

            // 4d. Calcular subtotal (precio actual del producto × cantidad)
            BigDecimal precioUnitario = producto.getPrecio();
            BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(item.getCantidad()));

            // 4e. Crear el detalle
            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubtotal(subtotal);

            venta.getDetalles().add(detalle);

            // 4f. Sumar al total
            total = total.add(subtotal);
        }

        // 5. Setear el total y guardar (los detalles se guardan en cascada)
        venta.setTotal(total);
        Venta guardada = ventaRepository.save(venta);

        return convertirAResponse(guardada);
    }

    public List<VentaResponseDTO> listar(Long sedeId) {
        List<Venta> ventas;
        if (sedeId != null) {
            ventas = ventaRepository.findBySedeId(sedeId);
        } else {
            ventas = ventaRepository.findAll();
        }
        return ventas.stream().map(this::convertirAResponse).toList();
    }

    public VentaResponseDTO buscarPorId(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con id: " + id));
        return convertirAResponse(venta);
    }
}