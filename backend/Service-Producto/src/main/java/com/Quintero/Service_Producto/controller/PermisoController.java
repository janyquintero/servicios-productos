package com.Quintero.Service_Producto.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

import com.Quintero.Service_Producto.dto.ProductoDTO;
import com.Quintero.Service_Producto.service.IProductoService;

public class PermisoController {
    private IProductoService productoService;

    @GetMapping
    public List<ProductoDTO> obtenerProductos() {
        return productoService.obtenerProductos();
    }
}
