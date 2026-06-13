package com.Quintero.Service_Producto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Quintero.Service_Producto.dto.RolesDTO;
import com.Quintero.Service_Producto.exception.NotFoundException;
import com.Quintero.Service_Producto.mapper.Mapper;
import com.Quintero.Service_Producto.model.Roles;
import com.Quintero.Service_Producto.repository.RolesRepository;

@Service
public class RolesService implements IRolesService {

    private RolesRepository rolesRepository;

    @Override
    public List<RolesDTO> obtenerRoles() {
        return rolesRepository.findAll().stream()
                .map(Mapper::toRolesDTO)
                .toList();
    }

    @Override
    public RolesDTO crearRole(RolesDTO roles) {
        Roles rol = Roles.builder()
                .roleName(roles.getRoleName())
                .descripcion(roles.getDescripcion())
                .estado(roles.getEstado())
                .build();
        return Mapper.toRolesDTO(rolesRepository.save(rol));
    }

    @Override
    public RolesDTO obtenerRoleById(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("El ID del rol no puede ser nulo.");
        }
        Roles rol = rolesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("El rol con ID " + id + " no existe."));
        return Mapper.toRolesDTO(rol);
    }

    @Override
    public RolesDTO actualizaRole(Long id, RolesDTO roles) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del rol no puede ser nulo.");
        }
        if (roles == null) {
            throw new IllegalArgumentException("El rol no puede ser nulo.");
        }
        Roles rol = rolesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("El rol con ID " + id + " no existe."));

        rol.setRoleName(roles.getRoleName());
        rol.setDescripcion(roles.getDescripcion());
        rol.setEstado(roles.getEstado());

        return Mapper.toRolesDTO(rolesRepository.save(rol));
    }

    @Override
    public void eliminarRole(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del rol no puede ser nulo.");
        }
        if (rolesRepository.existsById(id)) {
            rolesRepository.deleteById(id);
        } else {
            throw new RuntimeException("El rol con ID " + id + " no existe.");
        }
    }

}
