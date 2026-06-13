package com.Quintero.Service_Producto.service;

import com.Quintero.Service_Producto.model.Permisos;
import com.Quintero.Service_Producto.repository.PermisoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PermisoService implements IPermisoService {

    private final PermisoRepository permisoRepository;

    public PermisoService(PermisoRepository permisoRepository) {
        this.permisoRepository = permisoRepository;
    }

    @Override
    public List<Permisos> obtenerPermisos() {
        return permisoRepository.findAll();
    }
}