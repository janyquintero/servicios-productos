package com.Quintero.Service_Producto.service;

import java.util.List;

import com.Quintero.Service_Producto.dto.UsuarioDTO;

public interface IUsuarioService {
    List<UsuarioDTO> obtenerUsuarios();

    UsuarioDTO obtenerUsuarioById(Long id);

    UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO);

    UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO);

    void eliminarUsuario(Long id);

}
