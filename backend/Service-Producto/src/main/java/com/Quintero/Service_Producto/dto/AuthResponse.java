package com.Quintero.Service_Producto.dto;

public record AuthResponse(String token,
    String tokenType, 
    long expiresIn) {
    
}
