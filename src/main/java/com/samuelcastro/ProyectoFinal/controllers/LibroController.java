package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.editors.MultipartFileToByteArrayEditor;
import com.samuelcastro.ProyectoFinal.entities.Libro;
import com.samuelcastro.ProyectoFinal.services.DepartamentoService;
import com.samuelcastro.ProyectoFinal.services.LibroService;
import com.samuelcastro.ProyectoFinal.services.PrestamoService;
import com.samuelcastro.ProyectoFinal.services.RegistroService;
import com.samuelcastro.ProyectoFinal.services.UsuarioDetails;
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

/**
 * Controlador para la gestión de libros.
 */
@Controller
@RequestMapping("/api/libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private RegistroService registroService;

    @Autowired
    private PrestamoService prestamoService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(byte[].class, new MultipartFileToByteArrayEditor());
    }

    /**
     * Lista todos los libros agrupados por título, autor y editorial.
     * Añade la cantidad de libros libres y la URL de la primera foto disponible de cada grupo.
     */
    @GetMapping
    public String getAllLibros(Model model) {
        List<Libro> libros = libroService.findAll();
        Map<String, List<Libro>> librosAgrupados = libros.stream()
                .collect(Collectors.groupingBy(
                        libro -> libro.getTitulo() + " - " + libro.getAutor() + " - " + libro.getEditorial()
                ));
        Map<String, Long> librosLibres = libros.stream()
                .filter(libro -> "Libre".equals(libro.getEstado()))
                .collect(Collectors.groupingBy(
                        libro -> libro.getTitulo() + " - " + libro.getAutor() + " - " + libro.getEditorial(),
                        Collectors.counting()
                ));

        // Lógica para seleccionar una imagen de un libro que tenga foto
        Map<String, String> fotosLibros = librosAgrupados.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .filter(libro -> libro.getFoto() != null)
                                .findFirst()
                                .map(libro -> "/api/libros/foto/" + libro.getIdLibro())
                                .orElse("")
                ));

        model.addAttribute("librosAgrupados", librosAgrupados);
        model.addAttribute("librosLibres", librosLibres);
        model.addAttribute("libros", libros);
        model.addAttribute("fotosLibros", fotosLibros);

        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "libros/libros";
    }

    /**
     * Muestra el formulario para crear un nuevo libro.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoLibro(Model model) {
        model.addAttribute("libro", new Libro());
        model.addAttribute("departamentos", departamentoService.findAll());
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "libros/alta-libro";
    }

    /**
     * Procesa la creación de un nuevo libro.
     * El estado inicial es "Libre".
     */
    @PostMapping
    public String crearLibro(@ModelAttribute Libro libro, @RequestParam("foto") MultipartFile foto, Model model) {
        libro.setEstado("Libre");
        if (!foto.isEmpty()) {
            try {
                libro.setFoto(foto.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            libro.setFoto(null); // Asegurarse de que la foto sea null si no se proporciona
        }
        libroService.save(libro);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        registroService.registrarOperacion("Libro", libro.getIdLibro(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA CREAR ", libroService.findById(libro.getIdLibro()).getTitulo());
        return "redirect:/api/libros";
    }

    /**
     * Muestra todos los libros asociados a una clave (título, autor, editorial).
     */
    @GetMapping("/editar/{key}")
    public String mostrarLibrosAsociados(@PathVariable String key, Model model) {
        String[] parts = key.split(" - ");
        String titulo = parts[0];
        String autor = parts[1];
        String editorial = parts[2];
        List<Libro> libros = libroService.findByTituloAndAutorAndEditorial(titulo, autor, editorial);
        model.addAttribute("libros", libros);
        model.addAttribute("key", key);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "libros/libros-asociados";
    }

    /**
     * Muestra el formulario para editar un libro específico.
     */
    @GetMapping("/editar/libro/{id}")
    public String mostrarFormularioEditarLibro(@PathVariable int id, Model model) {
        Libro libro = libroService.findById(id);
        model.addAttribute("libro", libro);
        model.addAttribute("departamentos", departamentoService.findAll());
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "libros/editar-libro";
    }

    /**
     * Actualiza los datos de un libro.
     */
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
            UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
            registroService.registrarOperacion("Libro", existingLibro.getIdLibro(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA ACTUALIZAR ", existingLibro.getTitulo());
        }
        return "redirect:/api/libros";
    }

    /**
     * Elimina todos los libros asociados a una clave (título, autor, editorial).
     * Reasigna los préstamos de esos libros.
     */
    @GetMapping("/eliminar/{key}")
    public String eliminarLibrosPorClave(@PathVariable String key) {
        String[] parts = key.split(" - ");
        if (parts.length < 3) {
            throw new IllegalArgumentException("La clave debe contener título, autor y editorial separados por ' - '");
        }
        String titulo = parts[0];
        String autor = parts[1];
        String editorial = parts[2];
        List<Libro> libros = libroService.findByTituloAndAutorAndEditorial(titulo, autor, editorial);
        for (Libro libro : libros) {
            UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
            registroService.registrarOperacion("Libro", libro.getIdLibro(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA ELIMINAR ", libro.getTitulo());
            prestamoService.reasignarPrestamosPorLibro(libro.getIdLibro(), "missingLibro");
            libroService.deleteById(libro.getIdLibro());
        }
        // Mantenerse en la misma vista de libros asociados
        return "redirect:/api/libros/editar/" + key;
    }

    /**
     * Elimina un libro por su ID y reasigna sus préstamos.
     */
    @GetMapping("/eliminar/libro/{id}")
    public String eliminarLibroPorId(@PathVariable int id, @RequestParam("key") String key) {
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        Libro libro = libroService.findById(id);
        registroService.registrarOperacion("Libro", libro.getIdLibro(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA ELIMINAR ", libro.getTitulo());
        prestamoService.reasignarPrestamosPorLibro(id, "missingLibro");
        libroService.deleteById(id);
        // Mantenerse en la misma vista de libros asociados
        return "redirect:/api/libros/editar/" + key;
    }

    /**
     * Devuelve la foto de un libro como respuesta HTTP.
     */
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

    /**
     * Agrega múltiples libros a partir de una lista de ISBNs para una clave dada.
     */
    @PostMapping("/agregar-multiples")
    public String agregarMultiplesLibros(@RequestParam("isbns") String isbns, @RequestParam("key") String key) {
        String[] isbnArray = isbns.split(",");
        // Extraer título, autor y editorial del key
        String[] parts = key.split(" - ");
        String titulo = parts.length > 0 ? parts[0] : "";
        String autor = parts.length > 1 ? parts[1] : "";
        String editorial = parts.length > 2 ? parts[2] : "";
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        for (String isbn : isbnArray) {
            Libro libro = new Libro();
            libro.setIsbn(isbn.trim());
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setEditorial(editorial);
            libro.setEstado("Libre");
            libroService.save(libro);
            if (usuarioDetails != null) {
                registroService.registrarOperacion("Libro", libro.getIdLibro(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA CREAR ", libro.getTitulo());
            }
        }
        return "redirect:/api/libros/editar/" + key;
    }
}