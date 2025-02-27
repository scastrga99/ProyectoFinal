package com.samuelcastro.ProyectoFinal.utils;

import com.samuelcastro.ProyectoFinal.services.UsuarioDetails;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static UsuarioDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UsuarioDetails) {
            return (UsuarioDetails) authentication.getPrincipal();
        }
        return null;
    }
}
