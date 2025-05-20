package com.samuelcastro.ProyectoFinal.controllers;


import com.samuelcastro.ProyectoFinal.entities.Prestamo;
import com.samuelcastro.ProyectoFinal.entities.Usuario;
import com.samuelcastro.ProyectoFinal.services.PrestamoService;
import com.samuelcastro.ProyectoFinal.services.UsuarioDetails;
import com.samuelcastro.ProyectoFinal.utils.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class IndexController {

    @Autowired
    private PrestamoService prestamoService;

    @GetMapping("/")
    public String index(Model model) {
        // ...existing code para roles y registros...

        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            Usuario usuario = usuarioDetails.getUsuario();
            model.addAttribute("usuario", usuario);
            model.addAttribute("roles", usuario.getRol());

            // Préstamos próximos a la fecha límite (solo para ADMIN o PROFESOR)
            if (usuario.getRol().contains("ROLE_ADMIN") || usuario.getRol().contains("ROLE_PROFESOR")) {
                List<Prestamo> prestamos = prestamoService.findByUsuarioRealizaId(usuario.getIdUsuario());
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
                        return dias >= 0 && dias <= 3;
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
        // ...existing code...
        return "index";
    }
}