package com.Quintero.Service_Producto.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.Quintero.Service_Producto.dto.RolesDTO;
import com.Quintero.Service_Producto.service.IRolesService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RolesController {

    private final IRolesService rolesService;

    @GetMapping
    public ResponseEntity<List<RolesDTO>> obtenerRoles() {
        List<RolesDTO> roles = rolesService.obtenerRoles();
        return ResponseEntity.ok(roles);
    }

    @PostMapping("crear")
    public ResponseEntity<RolesDTO> crearRole(@RequestBody RolesDTO roleDTO) {
        RolesDTO nuevoRole = rolesService.crearRole(roleDTO);
        return ResponseEntity.ok(nuevoRole);
    }

    @PatchMapping("actualizar/{id}")
    public ResponseEntity<RolesDTO> actualizarRoles(@PathVariable Long id, @RequestBody RolesDTO roleDTO) {
        RolesDTO roleActualizado = rolesService.actualizaRole(id, roleDTO);
        return ResponseEntity.ok(roleActualizado);
    }

    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<Void> eliminarRoles(@PathVariable Long id) {
        rolesService.eliminarRole(id);
        return ResponseEntity.noContent().build();
    }
}
