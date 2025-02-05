package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.entities.Prestamo;
import com.samuelcastro.ProyectoFinal.services.AlumnoService;
import com.samuelcastro.ProyectoFinal.services.LibroService;
import com.samuelcastro.ProyectoFinal.services.PrestamoService;
import com.samuelcastro.ProyectoFinal.services.ProfesorDetails;
import com.samuelcastro.ProyectoFinal.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private LibroService libroService;

    @GetMapping
    public String getAllPrestamos(Model model) {
        List<Prestamo> prestamos = prestamoService.findAll();
        model.addAttribute("prestamos", prestamos);
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        if (profesorDetails != null) {
            model.addAttribute("profesor", profesorDetails.getProfesor());
        }
        return "prestamos/prestamos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoPrestamo(Model model) {
        model.addAttribute("prestamo", new Prestamo());
        model.addAttribute("alumnos", alumnoService.findAll());
        model.addAttribute("libros", libroService.findAll());
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        if (profesorDetails != null) {
            model.addAttribute("profesor", profesorDetails.getProfesor());
        }
        return "prestamos/alta-prestamo";
    }

    @PostMapping
    public String crearPrestamo(@ModelAttribute Prestamo prestamo) {
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        if (profesorDetails != null) {
            prestamo.setProfesor(profesorDetails.getProfesor());
        }
        prestamo.setFechaPrestamo(new Date());
        prestamoService.save(prestamo);
        return "redirect:/api/prestamos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPrestamo(@PathVariable int id) {
        prestamoService.deleteById(id);
        return "redirect:/api/prestamos";
    }
}