package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.entities.Prestamo;
import com.samuelcastro.ProyectoFinal.entities.Libro;
import com.samuelcastro.ProyectoFinal.services.UsuarioService;
import com.samuelcastro.ProyectoFinal.services.LibroService;
import com.samuelcastro.ProyectoFinal.services.PrestamoService;
import com.samuelcastro.ProyectoFinal.services.RegistroService;
import com.samuelcastro.ProyectoFinal.services.UsuarioDetails;
import com.samuelcastro.ProyectoFinal.services.EmailService;
import com.samuelcastro.ProyectoFinal.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private LibroService libroService;

    @Autowired
    private RegistroService registroService;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public String getAllPrestamos(Model model) {
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        List<Prestamo> prestamos;
        if (usuarioDetails != null && "ROLE_USER".equals(usuarioDetails.getUsuario().getRol())) {
            // Solo préstamos donde el usuario es quien realiza o recibe
            prestamos = prestamoService.findByUsuarioId(usuarioDetails.getUsuario().getIdUsuario());
        } else {
            // Admin y profesor ven todos
            prestamos = prestamoService.findAll();
        }
        model.addAttribute("prestamos", prestamos);
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "prestamos/prestamos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoPrestamo(Model model) {
        model.addAttribute("prestamo", new Prestamo());
        model.addAttribute("usuarios", usuarioService.findAll());
        Map<String, List<Libro>> librosAgrupados = libroService.findAll().stream()
                .filter(libro -> "Libre".equals(libro.getEstado()))
                .collect(Collectors.groupingBy(
                        libro -> libro.getTitulo() + " - " + libro.getAutor() + " - " + libro.getEditorial()
                ));
        model.addAttribute("librosAgrupados", librosAgrupados);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "prestamos/alta-prestamo";
    }

    @PostMapping
    public String crearPrestamo(@ModelAttribute Prestamo prestamo) {
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            prestamo.setUsuarioRealiza(usuarioDetails.getUsuario());
        }
        prestamo.setFechaPrestamo(new Date());
        Libro libro = libroService.findById(prestamo.getLibro().getIdLibro());
        libro.setEstado("Prestado");
        libroService.save(libro);
        prestamoService.save(prestamo);
        if (usuarioDetails != null) {
            registroService.registrarOperacion("Prestamo", prestamo.getIdPrestamo(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA CREAR SOBRE ", libro.getTitulo());
        }
        return "redirect:/api/prestamos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarPrestamo(@PathVariable int id, Model model) {
        Prestamo prestamo = prestamoService.findById(id);
        model.addAttribute("prestamo", prestamo);
        model.addAttribute("usuarios", usuarioService.findAll());
        model.addAttribute("libros", libroService.findAll());
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
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
            UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
            if (usuarioDetails != null) {
                registroService.registrarOperacion("Prestamo", existingPrestamo.getIdPrestamo(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA ACTUALIZAR SOBRE ", libroService.findById(existingPrestamo.getLibro().getIdLibro()).getTitulo());
            }
        }
        return "redirect:/api/prestamos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPrestamo(@PathVariable int id) {
        Prestamo prestamo = prestamoService.findById(id);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (prestamo != null) {
            prestamoService.deleteById(id);
            if (usuarioDetails != null) {
                registroService.registrarOperacion("Prestamo", id, usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA ELIMINAR SOBRE ", libroService.findById(prestamo.getLibro().getIdLibro()).getTitulo());
            }
        }
        return "redirect:/api/prestamos";
    }

    @GetMapping("/devolver/{id}")
    public String devolverPrestamo(@PathVariable int id) {
        Prestamo prestamo = prestamoService.findById(id);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (prestamo != null) {
            prestamo.setDevuelto(true);
            prestamo.setFechaDevolucion(new Date());
            Libro libro = libroService.findById(prestamo.getLibro().getIdLibro());
            libro.setEstado("Libre");
            libroService.save(libro);
            prestamoService.save(prestamo);
            if (usuarioDetails != null) {
                registroService.registrarOperacion("Prestamo", prestamo.getIdPrestamo(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA DEVOLVER SOBRE ", libroService.findById(prestamo.getLibro().getIdLibro()).getTitulo());
            }
        }
        return "redirect:/api/prestamos";
    }

    @GetMapping("/recordatorio/{id}")
    public String enviarRecordatorio(@PathVariable int id) {
        Prestamo prestamo = prestamoService.findById(id);
        if (prestamo != null && prestamo.getUsuarioRecibe().getRol().equals("ROLE_USER")) {
            String fechaPlazoStr = new java.text.SimpleDateFormat("dd/MM/yyyy").format(prestamo.getFechaPlazo());
            emailService.enviarRecordatorio(prestamo.getUsuarioRecibe().getCorreo(), prestamo.getUsuarioRecibe().getNombre() + " " + prestamo.getUsuarioRecibe().getApellidos(), prestamo.getLibro().getTitulo(), fechaPlazoStr);
        }
        return "redirect:/api/prestamos";
    }
}