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

    @GetMapping("/")
    public String index(Model model) {
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            Usuario usuario = usuarioDetails.getUsuario();
            model.addAttribute("usuario", usuario);
            model.addAttribute("roles", usuario.getRol());

            model.addAttribute("registros", registroService.findAll());

            // Préstamos próximos a la fecha límite (solo para ADMIN o PROFESOR)
            if (usuario.getRol().contains("ROLE_ADMIN") || usuario.getRol().contains("ROLE_PROFESOR")) {
                // Obtiene todos los préstamos realizados por el usuario actual
                List<Prestamo> prestamos = prestamoService.findByUsuarioRealizaId(usuario.getIdUsuario());
                // Filtra los préstamos para quedarse solo con los que:
                List<Prestamo> proximos = prestamos.stream()
                    // No han sido devueltos
                    .filter(p -> !p.isDevuelto())
                    // La fecha límite está entre hoy y 3 días adelante (inclusive)
                    .filter(p -> {
                        LocalDate fechaPlazo = null;
                        // Convierte la fecha de plazo a LocalDate según el tipo de dato
                        if (p.getFechaPlazo() instanceof java.sql.Date) {
                            fechaPlazo = ((java.sql.Date) p.getFechaPlazo()).toLocalDate();
                        } else if (p.getFechaPlazo() instanceof java.util.Date) {
                            fechaPlazo = new java.sql.Date(p.getFechaPlazo().getTime()).toLocalDate();
                        }
                        // Calcula los días entre hoy y la fecha límite
                        long dias = ChronoUnit.DAYS.between(LocalDate.now(), fechaPlazo);
                        // Solo incluye si faltan entre 0 y 3 días
                        return dias >= 0 && dias <= 3;
                    })
                    // Para cada préstamo, calcula y asigna los días restantes
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
                    // Convierte el stream filtrado y mapeado a una lista
                    .collect(Collectors.toList());
                // Agrega la lista de préstamos próximos al modelo para mostrarla en la vista
                model.addAttribute("prestamosProximos", proximos);
            }
        }
        return "index";
    }
}