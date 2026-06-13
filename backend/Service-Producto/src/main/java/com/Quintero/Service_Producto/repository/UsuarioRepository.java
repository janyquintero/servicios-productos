package com.Quintero.Service_Producto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Quintero.Service_Producto.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
}
