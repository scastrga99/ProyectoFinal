package com.samuelcastro.ProyectoFinal.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * Maneja las solicitudes GET a la raíz ("/") de la aplicación.
     * 
     * @return El nombre de la vista "index.html".
     */
    @GetMapping("/")
    public String home() {
        return "index.html";
    }

}
