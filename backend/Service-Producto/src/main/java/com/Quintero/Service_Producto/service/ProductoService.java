package com.Quintero.Service_Producto.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.Quintero.Service_Producto.dto.ProductoDTO;
import com.Quintero.Service_Producto.exception.NotFoundException;
import com.Quintero.Service_Producto.mapper.Mapper;
import com.Quintero.Service_Producto.model.Marca;
import com.Quintero.Service_Producto.model.Producto;
import com.Quintero.Service_Producto.repository.MarcaRepository;
import com.Quintero.Service_Producto.repository.ProductoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoService implements IProductoService {

    private final ProductoRepository productoRepository;

    private final MarcaRepository marcaRepository;

    @Override
    public ProductoDTO crearProducto(ProductoDTO producto) {
        log.info("inicia a crear el producto ");
        if (producto == null) {
            log.info("El producto no puede ser nulo. ");
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }

        if (producto.getSku() == null || producto.getSku().isBlank()) {
            log.info("El SKU es obligatorio. ");
            throw new IllegalArgumentException("El SKU es obligatorio.");
        }

        if (productoRepository.existsBySku(producto.getSku())) {
            log.info("Ya existe un producto con el SKU: " + producto.getSku());
            throw new IllegalArgumentException("Ya existe un producto con el SKU: " + producto.getSku());
        }
        Producto produc = Producto.builder()
                .sku(producto.getSku())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(BigDecimal.valueOf(producto.getPrecio()))
                .marca(producto.getMarcaId() != null ? marcaRepository.findById(producto.getMarcaId())
                        .orElseThrow(
                                () -> new NotFoundException("Marca no encontrada con ID: " + producto.getMarcaId()))
                        : null)
                .build();
        log.info("Producto creado exitosamente ");
        return Mapper.toProductoDTO(productoRepository.save(produc));
    }

    @Override
    public ProductoDTO actualizarProducto(Long id, ProductoDTO producto) {
        log.info("Iniciando actualización de precio. Producto ID: " + producto);
        if (id == null) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo.");
        }
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        Producto product = productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));

        product.setNombre(producto.getNombre());
        product.setDescripcion(producto.getDescripcion());
        product.setPrecio(BigDecimal.valueOf(producto.getPrecio()));
        Marca marca = null;
        if (producto.getMarcaId() != null) {
            marca = marcaRepository.findById(producto.getMarcaId())
                    .orElseThrow(() -> new NotFoundException("Marca no encontrada con ID: " + producto.getMarcaId()));
        }
        product.setMarca(marca);
        log.info("Producto actualizado exitosamente para el producto: {}", id);
        return Mapper.toProductoDTO(productoRepository.save(product));
    }

    @Override
    public ProductoDTO obtenerProductoId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo.");
        }
        return Mapper.toProductoDTO(productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id)));
    }

    @Override
    public List<ProductoDTO> obtenerProductos() {
        return productoRepository.findAll().stream()
                .map(Mapper::toProductoDTO)
                .toList();
    }

    @Transactional
    public void eliminarProducto(Long id) {
        log.info("El producto sera eliminado. ");
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        producto.setEstado("ELIMINADO");
        log.info("El producto cambio el estado a eliminado ");
        productoRepository.save(producto);
    }
}
