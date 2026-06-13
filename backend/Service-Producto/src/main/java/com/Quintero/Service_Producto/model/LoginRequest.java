package com.Quintero.Service_Producto.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder

public class LoginRequest {
    private String email;
    private String password;
}
