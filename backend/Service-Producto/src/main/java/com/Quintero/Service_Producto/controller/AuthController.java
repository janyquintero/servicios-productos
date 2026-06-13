package com.Quintero.Service_Producto.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Quintero.Service_Producto.dto.AuthResponse;
import com.Quintero.Service_Producto.service.AuthService;

import lombok.RequiredArgsConstructor;

import com.Quintero.Service_Producto.dto.RegisterRequest;
import com.Quintero.Service_Producto.model.LoginRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {

        AuthResponse authResponse = authService.login(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/registrar")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {

        AuthResponse authResponse = authService.registrar(registerRequest);
        return ResponseEntity.ok(authResponse);
    }
}
