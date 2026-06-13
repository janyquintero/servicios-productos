package com.Quintero.Service_Producto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Quintero.Service_Producto.dto.UsuarioDTO;
import com.Quintero.Service_Producto.mapper.Mapper;
import com.Quintero.Service_Producto.model.Roles;
import com.Quintero.Service_Producto.model.Usuario;
import com.Quintero.Service_Producto.repository.RolesRepository;
import com.Quintero.Service_Producto.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UsuarioDTO> obtenerUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(Mapper::toUsuarioDTO)
                .toList();
    }

    @Override
    public UsuarioDTO crearUsuario(UsuarioDTO usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        String encodedPassword = passwordEncoder.encode(usuario.getPassword());

        Roles rol = rolesRepository.findById(usuario.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + usuario.getRolId()));

        Usuario usu = Usuario.builder()
                .email(usuario.getEmail())
                .password(encodedPassword)
                .estado(usuario.getEstado())
                .roles(rol)
                .build();
        return Mapper.toUsuarioDTO(usuarioRepository.save(usu));
    }

    @Override
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuario) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        Usuario usu = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        usu.setEmail(usuario.getEmail());
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(usuario.getPassword());
            usu.setPassword(encodedPassword);
        }
        usu.setEstado(usuario.getEstado());
        if (usuario.getRolId() != null) {
            Roles rol = rolesRepository.findById(usuario.getRolId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + usuario.getRolId()));
            usu.setRoles(rol);
        }

        return Mapper.toUsuarioDTO(usuarioRepository.save(usu));
    }

    @Override
    public UsuarioDTO obtenerUsuarioById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        return Mapper.toUsuarioDTO(usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id)));
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        usuario.setEstado("ELIMINADO");
        usuarioRepository.save(usuario);
    }
}
