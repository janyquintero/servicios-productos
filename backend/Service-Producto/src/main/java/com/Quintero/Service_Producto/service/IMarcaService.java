package com.Quintero.Service_Producto.service;

import java.util.List;

import com.Quintero.Service_Producto.dto.MarcaDTO;
import com.Quintero.Service_Producto.model.Marca;

public interface IMarcaService {

    List<Marca> TraerMarcas();

    MarcaDTO ObtenerMarcaById(Long id);

    MarcaDTO crearMarca(MarcaDTO marcaDTO);

    MarcaDTO ActualizarMarca(Long id, MarcaDTO marcaDTO);

    void EliminarMarca(Long id);
}
