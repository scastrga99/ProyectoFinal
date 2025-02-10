package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.entities.Prestamo;
import com.samuelcastro.ProyectoFinal.services.AlumnoService;
import com.samuelcastro.ProyectoFinal.services.LibroService;
import com.samuelcastro.ProyectoFinal.services.PrestamoService;
import com.samuelcastro.ProyectoFinal.services.RegistroService;
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

    @Autowired
    private RegistroService registroService;

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
        registroService.registrarOperacion("Prestamo", prestamo.getIdPrestamo(), prestamo.getProfesor().getNombre() + " " + prestamo.getProfesor().getApellidos() + " EJECUTA CREAR SOBRE ", libroService.findById(prestamo.getLibro().getIdLibro()).getTitulo());
        prestamoService.save(prestamo);
        return "redirect:/api/prestamos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarPrestamo(@PathVariable int id, Model model) {
        Prestamo prestamo = prestamoService.findById(id);
        model.addAttribute("prestamo", prestamo);
        model.addAttribute("alumnos", alumnoService.findAll());
        model.addAttribute("libros", libroService.findAll());
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        if (profesorDetails != null) {
            model.addAttribute("profesor", profesorDetails.getProfesor());
        }
        return "prestamos/editar-prestamo";
    }

    @PostMapping("/{id}")
    public String actualizarPrestamo(@PathVariable int id, @ModelAttribute Prestamo prestamo) {
        Prestamo existingPrestamo = prestamoService.findById(id);
        if (existingPrestamo != null) {
            existingPrestamo.setFechaPrestamo(prestamo.getFechaPrestamo());
            existingPrestamo.setFechaPlazo(prestamo.getFechaPlazo());
            existingPrestamo.setFechaDevolucion(prestamo.getFechaDevolucion());
            existingPrestamo.setDevuelto(prestamo.isDevuelto());
            prestamoService.save(existingPrestamo);
            ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
            registroService.registrarOperacion("Prestamo", existingPrestamo.getIdPrestamo(), profesorDetails.getProfesor().getNombre() + " " + profesorDetails.getProfesor().getApellidos() + " EJECUTA ACTUALIZAR SOBRE ", libroService.findById(existingPrestamo.getLibro().getIdLibro()).getTitulo());
        }
        return "redirect:/api/prestamos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPrestamo(@PathVariable int id) {
        Prestamo prestamo = prestamoService.findById(id);
        if (prestamo != null) {
            prestamoService.deleteById(id);
            ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
            registroService.registrarOperacion("Prestamo", id, profesorDetails.getProfesor().getNombre() + " " + profesorDetails.getProfesor().getApellidos() + " EJECUTA ELIMINAR SOBRE ", libroService.findById(prestamo.getLibro().getIdLibro()).getTitulo());
        }
        return "redirect:/api/prestamos";
    }

    @GetMapping("/devolver/{id}")
    public String devolverPrestamo(@PathVariable int id) {
        Prestamo prestamo = prestamoService.findById(id);
        if (prestamo != null) {
            prestamo.setDevuelto(true);
            prestamo.setFechaDevolucion(new Date());
            prestamoService.save(prestamo);
            ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
            registroService.registrarOperacion("Prestamo", prestamo.getIdPrestamo(), profesorDetails.getProfesor().getNombre() + " " + profesorDetails.getProfesor().getApellidos() + " EJECUTA DEVOLVER SOBRE ", libroService.findById(prestamo.getLibro().getIdLibro()).getTitulo());
        }
        return "redirect:/api/prestamos";
    }
}