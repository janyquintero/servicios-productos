package com.Quintero.Service_Producto.config;

import org.springframework.boot.CommandLineRunner;

import com.Quintero.Service_Producto.model.Permisos;
import com.Quintero.Service_Producto.model.Roles;
import com.Quintero.Service_Producto.repository.PermisoRepository;
import com.Quintero.Service_Producto.repository.RolesRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final RolesRepository rolesRepository;
    private final PermisoRepository permisosRepository;

    @Override
    public void run(String... args) throws Exception {
        if (rolesRepository.findByRoleName("ADMIN") == null) {
            Roles adminRol = new Roles();
            adminRol.setRoleName("ADMIN");
            rolesRepository.save(adminRol);
        }
        if (rolesRepository.findByRoleName("USER") == null) {
            Roles userRol = new Roles();
            userRol.setRoleName("USER");
            rolesRepository.save(userRol);
        }

        boolean existeGetUsuarios = permisosRepository.findAll().stream()
                .anyMatch(
                        p -> p.getUrlpermiso().equals("/api/v1/usuarios") && p.getHttpMethod().equalsIgnoreCase("GET"));

        if (!existeGetUsuarios) {
            Permisos listarUsuarios = new Permisos();
            listarUsuarios.setUrlpermiso("/api/v1/usuarios");
            listarUsuarios.setHttpMethod("GET");
            listarUsuarios.setRolename("USER");
            permisosRepository.save(listarUsuarios);
            System.out.println("======> DataLoader: Permiso GET /api/v1/usuarios creado.");
        }

        String rutaEscritura = "/api/v1/usuarios/**";

        boolean existePostUsuarios = permisosRepository.findAll().stream()
                .anyMatch(p -> p.getUrlpermiso().equals(rutaEscritura) && p.getHttpMethod().equalsIgnoreCase("POST"));
        if (!existePostUsuarios) {
            Permisos crear = new Permisos();
            crear.setUrlpermiso(rutaEscritura);
            crear.setHttpMethod("POST");
            crear.setRolename("ADMIN");
            permisosRepository.save(crear);
        }

        boolean existePatchUsuarios = permisosRepository.findAll().stream()
                .anyMatch(p -> p.getUrlpermiso().equals(rutaEscritura) && p.getHttpMethod().equalsIgnoreCase("PATCH"));
        if (!existePatchUsuarios) {
            Permisos actualizar = new Permisos();
            actualizar.setUrlpermiso(rutaEscritura);
            actualizar.setHttpMethod("PATCH");
            actualizar.setRolename("ADMIN");
            permisosRepository.save(actualizar);
        }

        boolean existeDeleteUsuarios = permisosRepository.findAll().stream()
                .anyMatch(p -> p.getUrlpermiso().equals(rutaEscritura) && p.getHttpMethod().equalsIgnoreCase("DELETE"));
        if (!existeDeleteUsuarios) {
            Permisos eliminar = new Permisos();
            eliminar.setUrlpermiso(rutaEscritura);
            eliminar.setHttpMethod("DELETE");
            eliminar.setRolename("ADMIN");
            permisosRepository.save(eliminar);
            System.out.println("======> Reglas dinámicas para /api/v1/usuarios mapeadas con éxito");
        }
    }
}
