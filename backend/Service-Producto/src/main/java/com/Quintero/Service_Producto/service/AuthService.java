package com.Quintero.Service_Producto.service;

import org.springframework.stereotype.Service;

import com.Quintero.Service_Producto.dto.AuthResponse;

import com.Quintero.Service_Producto.dto.RegisterRequest;
import com.Quintero.Service_Producto.model.LoginRequest;
import com.Quintero.Service_Producto.model.Roles;
import com.Quintero.Service_Producto.model.Usuario;
import com.Quintero.Service_Producto.repository.RolesRepository;
import com.Quintero.Service_Producto.repository.UsuarioRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service

public class AuthService {

    private final UsuarioRepository usuarioRepository;

    private final RolesRepository rolesRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthService(
            UsuarioRepository usuarioRepository,
            RolesRepository rolesRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {

        this.usuarioRepository = usuarioRepository;
        this.rolesRepository = rolesRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            String token = jwtService.generateToken(usuario);

            java.util.Date expirationDate = jwtService.getExpirationTime(token);

            long expiresIn = (expirationDate != null) ? expirationDate.getTime() : 3600000; // 1 hora por defecto si es
            return new AuthResponse(token, "Bearer", expiresIn);

        } catch (Exception e) {
            System.err.println("=== [ERROR CRÍTICO EN LOGIN]: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public AuthResponse registrar(RegisterRequest registerRequest) {

        if (usuarioRepository.findByEmail(registerRequest.email()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmail(registerRequest.email());
        nuevoUsuario.setPassword(passwordEncoder.encode(registerRequest.password()));

        Roles rol = rolesRepository.findByRoleName("USER")
                .orElseGet(() -> {
                    Roles nuevoRol = new Roles();
                    nuevoRol.setRoleName("USER");
                    return rolesRepository.save(nuevoRol);
                });
        nuevoUsuario.setRoles(rol);

        usuarioRepository.save(nuevoUsuario);

        String token = jwtService.generateToken(nuevoUsuario);
        long expiresIn = jwtService.getExpirationTime(token).getTime();

        return new AuthResponse(token, "Bearer", expiresIn);
    }

}
