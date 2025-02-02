package com.samuelcastro.ProyectoFinal.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index.html"; // Asegúrate de que la vista "index.html" esté en la carpeta "templates"
    }

}
