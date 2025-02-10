package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.entities.Libro;
import com.samuelcastro.ProyectoFinal.services.DepartamentoService;
import com.samuelcastro.ProyectoFinal.services.LibroService;
import com.samuelcastro.ProyectoFinal.services.RegistroService;
import com.samuelcastro.ProyectoFinal.services.ProfesorDetails;
import com.samuelcastro.ProyectoFinal.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private RegistroService registroService;

    @GetMapping
    public String getAllLibros(Model model) {
        List<Libro> libros = libroService.findAll();
        model.addAttribute("libros", libros);
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        if (profesorDetails != null) {
            model.addAttribute("profesor", profesorDetails.getProfesor());
        }
        return "libros/libros";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoLibro(Model model) {
        model.addAttribute("libro", new Libro());
        model.addAttribute("departamentos", departamentoService.findAll());
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        if (profesorDetails != null) {
            model.addAttribute("profesor", profesorDetails.getProfesor());
        }
        return "libros/alta-libro";
    }

    @PostMapping
    public String crearLibro(@ModelAttribute Libro libro) {
        libroService.save(libro);
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        registroService.registrarOperacion("Libro", libro.getIdLibro(), profesorDetails.getProfesor().getNombre() + " " + profesorDetails.getProfesor().getApellidos() + " EJECUTA CREAR ", libroService.findById(libro.getIdLibro()).getTitulo());
        return "redirect:/api/libros";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarLibro(@PathVariable int id, Model model) {
        Libro libro = libroService.findById(id);
        model.addAttribute("libro", libro);
        model.addAttribute("departamentos", departamentoService.findAll());
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        if (profesorDetails != null) {
            model.addAttribute("profesor", profesorDetails.getProfesor());
        }
        return "libros/editar-libro";
    }

    @PostMapping("/{id}")
    public String actualizarLibro(@PathVariable int id, @ModelAttribute Libro libro) {
        Libro existingLibro = libroService.findById(id);
        if (existingLibro != null) {
            existingLibro.setIsbn(libro.getIsbn());
            existingLibro.setTitulo(libro.getTitulo());
            existingLibro.setAutor(libro.getAutor());
            existingLibro.setEditorial(libro.getEditorial());
            existingLibro.setEstado(libro.getEstado());
            libroService.save(existingLibro);
            ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
            registroService.registrarOperacion("Libro", existingLibro.getIdLibro(), profesorDetails.getProfesor().getNombre() + " " + profesorDetails.getProfesor().getApellidos() + " EJECUTA ACTUALIZAR ", existingLibro.getTitulo());
        }
        return "redirect:/api/libros";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarLibro(@PathVariable int id) {
        Libro libro = libroService.findById(id);
        if (libro != null) {
            libroService.deleteById(id);
            ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
            registroService.registrarOperacion("Libro", id, profesorDetails.getProfesor().getNombre() + " " + profesorDetails.getProfesor().getApellidos() + " EJECUTA ELIMINAR ", libro.getTitulo());
        }
        return "redirect:/api/libros";
    }
}
