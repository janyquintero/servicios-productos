package com.Quintero.Service_Producto.service;

import java.util.List;

import com.Quintero.Service_Producto.dto.ProductoDTO;

public interface IProductoService {

    List<ProductoDTO> obtenerProductos();

    ProductoDTO obtenerProductoId(Long id);

    ProductoDTO crearProducto(ProductoDTO productoDTO);

    ProductoDTO actualizarProducto(Long id, ProductoDTO productoDTO);

    void eliminarProducto(Long id);

}
