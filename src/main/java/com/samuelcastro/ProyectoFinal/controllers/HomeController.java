package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.services.ProfesorDetails;
import com.samuelcastro.ProyectoFinal.utils.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        if (profesorDetails != null) {
            model.addAttribute("profesor", profesorDetails.getProfesor());
        }
        return "index";
    }
}
