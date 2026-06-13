package com.Quintero.Service_Producto.security;

import java.util.List;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.Quintero.Service_Producto.model.Permisos;
import com.Quintero.Service_Producto.service.PermisoService;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class FiltroSeguridadDinamica {

    private final PermisoService permisoService;

    public FiltroSeguridadDinamica(PermisoService permisoService) {
        this.permisoService = permisoService;
    }

    public AuthorizationDecision revisarAcceso(HttpServletRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        String path = request.getRequestURI();
        String metodo = request.getMethod();

        List<Permisos> reglas = permisoService.obtenerPermisos();
        AntPathMatcher pathMatcher = new AntPathMatcher();

        for (Permisos permiso : reglas) {

            boolean coincideUrl = pathMatcher.match(permiso.getUrlpermiso(), path);
            boolean coincideMetodo = permiso.getHttpMethod().equalsIgnoreCase(metodo);

            if (coincideUrl && coincideMetodo) {

                boolean tieneRol = authentication.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals(permiso.getRolename()));

                if (tieneRol) {
                    return new AuthorizationDecision(true);
                }
            }
        }
        return new AuthorizationDecision(false);
    }
}
