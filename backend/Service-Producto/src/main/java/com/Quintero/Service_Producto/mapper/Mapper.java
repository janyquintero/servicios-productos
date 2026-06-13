package com.Quintero.Service_Producto.mapper;

import com.Quintero.Service_Producto.dto.MarcaDTO;
import com.Quintero.Service_Producto.dto.ProductoDTO;
import com.Quintero.Service_Producto.dto.RolesDTO;
import com.Quintero.Service_Producto.dto.UsuarioDTO;
import com.Quintero.Service_Producto.model.Marca;
import com.Quintero.Service_Producto.model.Producto;
import com.Quintero.Service_Producto.model.Roles;
import com.Quintero.Service_Producto.model.Usuario;

public class Mapper {
    public static UsuarioDTO toUsuarioDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .password(usuario.getPassword())
                .estado(usuario.getEstado())
                .rolId(usuario.getRoles() != null ? usuario.getRoles().getId() : null)
                .build();
    }

    public static ProductoDTO toProductoDTO(Producto producto) {
        if (producto == null) {
            return null;
        }
        return ProductoDTO.builder()
                .id(producto.getId())
                .sku(producto.getSku())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio() != null ? producto.getPrecio().doubleValue() : null)
                .marcaId(producto.getMarca() != null ? producto.getMarca().getId() : null)
                .marcaNombre(producto.getMarca() != null ? producto.getMarca().getNombre() : null) // Opcional:
                                                                                                   // aprovecha tu campo
                                                                                                   // marcaNombre
                .build();
    }

    public static MarcaDTO toMarcaDTO(Marca marca) {
        if (marca == null) {
            return null;
        }
        return MarcaDTO.builder()
                .nombre(marca.getNombre())
                .descripcion(marca.getDescripcion())
                .build();
    }

    public static RolesDTO toRolesDTO(Roles roles) {
        if (roles == null) {
            return null;
        }
        return RolesDTO.builder()
                .roleName(roles.getRoleName())
                .descripcion(roles.getDescripcion())
                .estado(roles.getEstado())
                .build();
    }

}
