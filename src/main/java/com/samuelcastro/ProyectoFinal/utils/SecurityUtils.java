package com.samuelcastro.ProyectoFinal.utils;

import com.samuelcastro.ProyectoFinal.services.ProfesorDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static ProfesorDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof ProfesorDetails) {
            return (ProfesorDetails) authentication.getPrincipal();
        }
        return null;
    }
}
