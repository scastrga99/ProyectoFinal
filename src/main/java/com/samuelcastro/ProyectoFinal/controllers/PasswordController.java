package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.entities.Usuario;
import com.samuelcastro.ProyectoFinal.services.EmailService;
import com.samuelcastro.ProyectoFinal.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class PasswordController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/forgot-password")
    public String mostrarFormularioRecuperacion() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String recuperarContraseña(@RequestParam("correo") String correo, Model model) {
        Usuario usuario = usuarioService.findByCorreo(correo);
        if (usuario != null) {
            String nuevaContraseña = UUID.randomUUID().toString().substring(0, 8);
            usuario.setPassword(nuevaContraseña);
            usuarioService.save(usuario);
            emailService.enviarNuevaContraseña(correo, nuevaContraseña);
            model.addAttribute("mensaje", "Se ha enviado una nueva contraseña a su correo electrónico.");
        } else {
            model.addAttribute("error", "No se encontró una cuenta con ese correo electrónico.");
        }
        return "forgot-password";
    }
}