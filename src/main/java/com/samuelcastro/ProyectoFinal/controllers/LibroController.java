package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.editors.MultipartFileToByteArrayEditor;
import com.samuelcastro.ProyectoFinal.entities.Libro;
import com.samuelcastro.ProyectoFinal.services.DepartamentoService;
import com.samuelcastro.ProyectoFinal.services.LibroService;
import com.samuelcastro.ProyectoFinal.services.RegistroService;
import com.samuelcastro.ProyectoFinal.services.ProfesorDetails;
import com.samuelcastro.ProyectoFinal.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private RegistroService registroService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(byte[].class, new MultipartFileToByteArrayEditor());
    }

    @GetMapping
    public String getAllLibros(Model model) {
        List<Libro> libros = libroService.findAll();
        Map<String, List<Libro>> librosAgrupados = libros.stream()
                .collect(Collectors.groupingBy(
                        libro -> libro.getTitulo() + " - " + libro.getAutor() + " - " + libro.getEditorial()
                ));
        model.addAttribute("librosAgrupados", librosAgrupados);
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
    public String crearLibro(@ModelAttribute Libro libro, Model model) {
        libro.setEstado("Libre");
        libroService.save(libro);
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        registroService.registrarOperacion("Libro", libro.getIdLibro(), profesorDetails.getProfesor().getNombre() + " " + profesorDetails.getProfesor().getApellidos() + " EJECUTA CREAR ", libroService.findById(libro.getIdLibro()).getTitulo());
        return "redirect:/api/libros";
    }

    @GetMapping("/editar/{key}")
    public String mostrarLibrosAsociados(@PathVariable String key, Model model) {
        String[] parts = key.split(" - ");
        String titulo = parts[0];
        String autor = parts[1];
        String editorial = parts[2];
        List<Libro> libros = libroService.findByTituloAndAutorAndEditorial(titulo, autor, editorial);
        model.addAttribute("libros", libros);
        model.addAttribute("key", key);
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        if (profesorDetails != null) {
            model.addAttribute("profesor", profesorDetails.getProfesor());
        }
        return "libros/libros-asociados";
    }

    @GetMapping("/editar/libro/{id}")
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
    public String actualizarLibro(@PathVariable int id, @ModelAttribute Libro libro, @RequestParam("foto") MultipartFile foto) {
        Libro existingLibro = libroService.findById(id);
        if (existingLibro != null) {
            existingLibro.setIsbn(libro.getIsbn());
            existingLibro.setTitulo(libro.getTitulo());
            existingLibro.setAutor(libro.getAutor());
            existingLibro.setEditorial(libro.getEditorial());
            existingLibro.setEstado(libro.getEstado());
            if (!foto.isEmpty()) {
                try {
                    existingLibro.setFoto(foto.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            libroService.save(existingLibro);
            ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
            registroService.registrarOperacion("Libro", existingLibro.getIdLibro(), profesorDetails.getProfesor().getNombre() + " " + profesorDetails.getProfesor().getApellidos() + " EJECUTA ACTUALIZAR ", existingLibro.getTitulo());
        }
        return "redirect:/api/libros";
    }

    @GetMapping("/eliminar/{key}")
    public String eliminarLibro(@PathVariable String key) {
        String[] parts = key.split(" - ");
        if (parts.length < 3) {
            throw new IllegalArgumentException("La clave debe contener título, autor y editorial separados por ' - '");
        }
        String titulo = parts[0];
        String autor = parts[1];
        String editorial = parts[2];
        List<Libro> libros = libroService.findByTituloAndAutorAndEditorial(titulo, autor, editorial);
        for (Libro libro : libros) {
            libroService.deleteById(libro.getIdLibro());
            ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
            registroService.registrarOperacion("Libro", libro.getIdLibro(), profesorDetails.getProfesor().getNombre() + " " + profesorDetails.getProfesor().getApellidos() + " EJECUTA ELIMINAR ", libro.getTitulo());
        }
        return "redirect:/api/libros";
    }

    @GetMapping("/foto/{id}")
    public ResponseEntity<byte[]> obtenerFoto(@PathVariable int id) {
        Libro libro = libroService.findById(id);
        if (libro != null && libro.getFoto() != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .body(libro.getFoto());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}