package com.samuelcastro.ProyectoFinal.controllers;


import com.samuelcastro.ProyectoFinal.entities.Prestamo;
import com.samuelcastro.ProyectoFinal.entities.Usuario;
import com.samuelcastro.ProyectoFinal.services.PrestamoService;
import com.samuelcastro.ProyectoFinal.services.RegistroService;
import com.samuelcastro.ProyectoFinal.services.UsuarioDetails;
import com.samuelcastro.ProyectoFinal.utils.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class IndexController {

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private RegistroService registroService;

    /**
     * Controlador para la página principal.
     * Si el usuario está autenticado, añade información relevante al modelo:
     * - Usuario y roles
     * - Registros de operaciones
     * - Préstamos próximos a vencer (según el rol)
     */
    @GetMapping("/")
    public String index(Model model) {
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            Usuario usuario = usuarioDetails.getUsuario();
            model.addAttribute("usuario", usuario);
            model.addAttribute("roles", usuario.getRol());

            model.addAttribute("registros", registroService.findAll());

            // Préstamos próximos a la fecha límite (para ADMIN, PROFESOR y USER)
            if (usuario.getRol().contains("ROLE_ADMIN") ||
                usuario.getRol().contains("ROLE_PROFESOR") ||
                usuario.getRol().contains("ROLE_USER")) {

                // Obtiene los préstamos asociados al usuario actual:
                // Si es solo USER, obtiene los préstamos donde es destinatario.
                // Si es ADMIN o PROFESOR, obtiene los préstamos realizados por él.
                List<Prestamo> prestamos;
                if (usuario.getRol().contains("ROLE_USER") && 
                    !usuario.getRol().contains("ROLE_ADMIN") && 
                    !usuario.getRol().contains("ROLE_PROFESOR")) {
                    // Solo ROLE_USER: préstamos donde el usuario es el que recibe
                    prestamos = prestamoService.findByUsuarioRecibeId(usuario.getIdUsuario());
                } else {
                    // ADMIN o PROFESOR (o ambos): préstamos realizados por el usuario actual
                    prestamos = prestamoService.findByUsuarioRealizaId(usuario.getIdUsuario());
                }

                // Filtra préstamos no devueltos y próximos a vencer (0-3 días)
                List<Prestamo> proximos = prestamos.stream()
                    .filter(p -> !p.isDevuelto())
                    .filter(p -> {
                        LocalDate fechaPlazo = null;
                        if (p.getFechaPlazo() instanceof java.sql.Date) {
                            fechaPlazo = ((java.sql.Date) p.getFechaPlazo()).toLocalDate();
                        } else if (p.getFechaPlazo() instanceof java.util.Date) {
                            fechaPlazo = new java.sql.Date(p.getFechaPlazo().getTime()).toLocalDate();
                        }
                        long dias = ChronoUnit.DAYS.between(LocalDate.now(), fechaPlazo);
                        // Incluir vencidos (dias < 0) y próximos a vencer (dias entre 0 y 3)
                        return dias <= 3;
                    })
                    .map(p -> {
                        LocalDate fechaPlazo = null;
                        if (p.getFechaPlazo() instanceof java.sql.Date) {
                            fechaPlazo = ((java.sql.Date) p.getFechaPlazo()).toLocalDate();
                        } else if (p.getFechaPlazo() instanceof java.util.Date) {
                            fechaPlazo = new java.sql.Date(p.getFechaPlazo().getTime()).toLocalDate();
                        }
                        long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), fechaPlazo);
                        p.setDiasRestantes(diasRestantes);
                        return p;
                    })
                    .collect(Collectors.toList());
                model.addAttribute("prestamosProximos", proximos);
            }
        }
        return "index";
    }
}