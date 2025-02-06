package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.entities.Registro;
import com.samuelcastro.ProyectoFinal.services.ProfesorDetails;
import com.samuelcastro.ProyectoFinal.services.RegistroService;
import com.samuelcastro.ProyectoFinal.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private RegistroService registroService;

    @GetMapping("/")
    public String home(Model model) {
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        if (profesorDetails != null) {
            model.addAttribute("profesor", profesorDetails.getProfesor());
            if (profesorDetails.getProfesor().getRol().equals("ROLE_ADMIN")) {
                List<Registro> registros = registroService.findAll();
                model.addAttribute("registros", registros);
            }
        }
        return "index";
    }
}
