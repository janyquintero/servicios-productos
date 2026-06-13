package com.Quintero.Service_Producto.service;

import java.util.List;

import com.Quintero.Service_Producto.dto.RolesDTO;

public interface IRolesService {

    List<RolesDTO> obtenerRoles();

    RolesDTO obtenerRoleById(Long id);

    RolesDTO crearRole(RolesDTO roleDTO);

    RolesDTO actualizaRole(Long id, RolesDTO roleDTO);

    void eliminarRole(Long id);
}
