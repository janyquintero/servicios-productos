package com.Quintero.Service_Producto.model;

import java.util.Collection;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Usuarios")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String estado;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id") // El nombre de la columna FK en tu tabla 'usuarios'
    private Roles roles;

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Define que la cuenta no ha expirado
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Define que la cuenta no está bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Define que las credenciales no han expirado
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.roles != null) {
            String nombreRol = this.roles.getRoleName();

            if (nombreRol != null) {
                if (nombreRol.startsWith("ROLE_")) {
                    nombreRol = nombreRol.substring(5); // Deja solo "ADMIN"
                }
                return List.of(new SimpleGrantedAuthority(nombreRol));
            }
        }
        return java.util.Collections.emptyList();
    }

}
