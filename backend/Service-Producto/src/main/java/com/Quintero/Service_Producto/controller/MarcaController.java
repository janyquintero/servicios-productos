package com.Quintero.Service_Producto.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Quintero.Service_Producto.model.Marca;
import com.Quintero.Service_Producto.service.IMarcaService;

@RestController
@RequestMapping("/api/v1/marcas")
public class MarcaController {
    private IMarcaService marcaService;

    @GetMapping
    public List<Marca> obtenerMarcas() {
        return marcaService.TraerMarcas();
    }

}
