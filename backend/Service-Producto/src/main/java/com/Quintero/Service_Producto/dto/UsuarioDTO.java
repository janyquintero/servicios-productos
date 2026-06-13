package com.Quintero.Service_Producto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UsuarioDTO {
    private long id;
    private String usuarioname;
    private String email;
    private String password;
    private String estado;
    private Long rolId;
    private String rolNombre;
}
