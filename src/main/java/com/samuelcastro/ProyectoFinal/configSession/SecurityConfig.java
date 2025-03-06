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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/", "/api/libros", "/api/libros/**", "/css/**", "/js/**", "/img/**").permitAll() // Permitir acceso sin autenticación
                    .requestMatchers("/api/usuarios", "/api/departamentos").hasAnyRole("ADMIN", "PROFESOR")
                    .requestMatchers("/api/materiales", "/api/libros", "/api/prestamos").hasAnyRole("USER", "ADMIN", "PROFESOR")
                    .anyRequest().authenticated() // Requerir autenticación para cualquier otra solicitud
            )
            .formLogin(formLogin ->
                formLogin
                    .loginPage("/login") // Página de inicio de sesión personalizada
                    .defaultSuccessUrl("/", true) // Redirigir a la página principal después de iniciar sesión
                    .permitAll()
            )
            .logout(logout ->
                logout
                    .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}