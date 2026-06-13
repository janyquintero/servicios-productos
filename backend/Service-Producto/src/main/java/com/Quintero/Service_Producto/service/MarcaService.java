package com.Quintero.Service_Producto.service;

import com.Quintero.Service_Producto.dto.MarcaDTO;
import com.Quintero.Service_Producto.mapper.Mapper;
import com.Quintero.Service_Producto.model.Marca;
import com.Quintero.Service_Producto.repository.MarcaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MarcaService implements IMarcaService {

    private final MarcaRepository marcaRepository;

    public MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    @Override
    public List<Marca> TraerMarcas() {

        return marcaRepository.findAll();
    }

    @Override
    public MarcaDTO ObtenerMarcaById(Long id) {
        return Mapper.toMarcaDTO(marcaRepository.findById(id).orElse(null));
    }

    @Override
    public MarcaDTO crearMarca(MarcaDTO marcaDTO) {

        return null;
    }

    @Override
    public MarcaDTO ActualizarMarca(Long id, MarcaDTO marcaDTO) {

        return null;
    }

    @Override
    public void EliminarMarca(Long id) {
        marcaRepository.deleteById(id);
    }
}