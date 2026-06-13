package com.Quintero.Service_Producto.security;

// use Spring Security's RequestAuthorizationContext for access checks
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.Quintero.Service_Producto.model.Permisos;
import com.Quintero.Service_Producto.service.PermisoService;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ObjectProvider<PermisoService> permisoServiceProvider;

    public SecurityConfig(ObjectProvider<PermisoService> permisoServiceProvider) {
        this.permisoServiceProvider = permisoServiceProvider;
    }

    // SOLUCIÓN: Usamos la clase exacta JwtAuthenticationFilter en el parámetro.
    // Spring Boot buscará la clase por tipo exacto y Tomcat no interferirá jamás.
    @Bean
    public FilterRegistrationBean<Filter> registration(JwtAuthenticationFilter jwtAuthFilter) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(jwtAuthFilter);
        registration.setEnabled(false); // Evita ejecuciones duplicadas en Tomcat
        return registration;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Ajusta según tus necesidades
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            JwtAuthenticationFilter jwtAuthFilter) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests(auth -> auth
                        .anyRequest().access((authenticationSupplier,
                                authorizationContext) -> revisarAcceso(authenticationSupplier, authorizationContext)));

        return http.build();
    }

    public AuthorizationDecision revisarAcceso(
            Supplier<? extends Authentication> authenticationSupplier,
            RequestAuthorizationContext authorizationContext) {

        HttpServletRequest request = authorizationContext.getRequest();
        String path = request.getRequestURI();
        String metodo = request.getMethod();

        if (path.startsWith("/api/v1/auth/") || path.equals("/error")) {
            return new AuthorizationDecision(true);
        }

        Authentication authentication = authenticationSupplier.get();
        if (authentication != null) {
            System.out.println("-> Roles del usuario en Contexto: " + authentication.getAuthorities());
        }
        if (authentication == null || !authentication.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        PermisoService permisoService = permisoServiceProvider.getIfAvailable();
        if (permisoService == null) {
            return new AuthorizationDecision(false);
        }

        List<Permisos> reglas = permisoService.obtenerPermisos();
        AntPathMatcher pathMatcher = new AntPathMatcher();

        for (Permisos permiso : reglas) {
            boolean coincideUrl = pathMatcher.match(permiso.getUrlpermiso(), path);
            boolean coincideMetodo = permiso.getHttpMethod().equalsIgnoreCase(metodo);

            if (coincideUrl && coincideMetodo) {

                boolean tieneRol = authentication.getAuthorities().stream()
                        .anyMatch(authority -> {
                            String authName = authority.getAuthority().replace("ROLE_", "");
                            String dbRole = permiso.getRolename().replace("ROLE_", "");

                            return authName.equalsIgnoreCase(dbRole);
                        });

                if (tieneRol) {
                    return new AuthorizationDecision(true);
                } else {
                    System.out
                            .println("   [ERROR] El usuario no cuenta con el rol requerido: " + permiso.getRolename());
                }
            }
        }
        System.out.println("====== [ACCESO DENEGADO - No se cumplieron las reglas] ======");
        return new AuthorizationDecision(false);
    }
}