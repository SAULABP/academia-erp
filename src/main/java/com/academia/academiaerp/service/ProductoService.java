package com.academia.academiaerp.service;

import com.academia.academiaerp.enums.CategoriaProducto;
import com.academia.academiaerp.exception.RecursoNoEncontradoException;
import com.academia.academiaerp.model.Producto;
import com.academia.academiaerp.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final FileStorageService fileStorageService;

    public ProductoService(ProductoRepository productoRepository, FileStorageService fileStorageService) {
        this.productoRepository = productoRepository;
        this.fileStorageService = fileStorageService;
    }

    public List<Producto> listar(CategoriaProducto categoria) {
        if (categoria != null) {
            return productoRepository.findByCategoria(categoria);
        }
        return productoRepository.findAll();
    }

    public List<Producto> buscar(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con id: " + id));
    }

    public Producto crear(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, Producto datos) {
        Producto producto = buscarPorId(id);
        producto.setNombre(datos.getNombre());
        producto.setCategoria(datos.getCategoria());
        producto.setPrecio(datos.getPrecio());
        producto.setStock(datos.getStock());
        return productoRepository.save(producto);
    }

    public void eliminar(Long id) {
        Producto producto = buscarPorId(id);
        productoRepository.delete(producto);
    }

    public Producto subirImagen(Long id, MultipartFile archivo) {
        Producto producto = buscarPorId(id);
        String rutaCompleta = fileStorageService.guardarEn(archivo, "productos");
        // rutaCompleta = "productos/uuid.jpg" -> guardamos solo "uuid.jpg"
        String soloNombre = rutaCompleta.substring(rutaCompleta.indexOf("/") + 1);
        producto.setImagenUrl(soloNombre);
        return productoRepository.save(producto);
    }

    public Resource cargarImagen(String nombreArchivo) {
        return fileStorageService.cargarRuta("productos/" + nombreArchivo);
    }
}