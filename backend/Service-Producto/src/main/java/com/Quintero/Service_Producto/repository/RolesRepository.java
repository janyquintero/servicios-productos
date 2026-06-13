package com.Quintero.Service_Producto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Quintero.Service_Producto.model.Roles;

public interface RolesRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByRoleName(String roleName);
}