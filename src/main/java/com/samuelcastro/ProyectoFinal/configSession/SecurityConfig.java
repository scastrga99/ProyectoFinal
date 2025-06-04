package com.samuelcastro.ProyectoFinal.configSession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad de Spring Security.
     * Define las rutas públicas, las rutas protegidas por roles y la configuración de login/logout.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    // Rutas públicas sin autenticación
                    .requestMatchers("/", "/api/libros", "/api/libros/**", "/css/**", "/js/**", "/img/**", "/forgot-password").permitAll()
                    // Rutas accesibles solo para ADMIN o PROFESOR
                    .requestMatchers("/api/usuarios", "/api/departamentos").hasAnyRole("ADMIN", "PROFESOR")
                    // Rutas accesibles para USER, ADMIN o PROFESOR
                    .requestMatchers("/api/materiales", "/api/libros", "/api/prestamos").hasAnyRole("USER", "ADMIN", "PROFESOR")
                    // Cualquier otra ruta requiere autenticación
                    .anyRequest().authenticated()
            )
            .formLogin(formLogin ->
                formLogin
                    // Página de login personalizada
                    .loginPage("/login")
                    // Redirección tras login exitoso
                    .defaultSuccessUrl("/", true)
                    .permitAll()
            )
            .logout(logout ->
                logout
                    .permitAll()
            );

        return http.build();
    }

    /**
     * Bean para codificar contraseñas usando BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean para obtener el AuthenticationManager de la configuración.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}