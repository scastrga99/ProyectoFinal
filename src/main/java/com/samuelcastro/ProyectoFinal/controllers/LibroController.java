package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.entities.Libro;
import com.samuelcastro.ProyectoFinal.services.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    /**
     * Obtener todos los libros.
     * 
     * @return Lista de todos los libros.
     */
    @GetMapping
    public List<Libro> getAllLibros() {
        return libroService.findAll();
    }

    /**
     * Obtener un libro por su ID.
     * 
     * @param id ID del libro.
     * @return El libro con el ID especificado.
     */
    @GetMapping("/{id}")
    public Libro getLibroById(@PathVariable int id) {
        return libroService.findById(id);
    }

    /**
     * Crear un nuevo libro.
     * 
     * @param libro Datos del nuevo libro.
     * @return El libro creado.
     */
    @PostMapping
    public Libro createLibro(@RequestBody Libro libro) {
        return libroService.save(libro);
    }

    /**
     * Actualizar un libro existente.
     * 
     * @param id ID del libro a actualizar.
     * @param libro Datos actualizados del libro.
     * @return El libro actualizado.
     */
    @PutMapping("/{id}")
    public Libro updateLibro(@PathVariable int id, @RequestBody Libro libro) {
        Libro existingLibro = libroService.findById(id);
        if (existingLibro != null) {
            existingLibro.setIsbn(libro.getIsbn());
            existingLibro.setTitulo(libro.getTitulo());
            existingLibro.setAutor(libro.getAutor());
            existingLibro.setEditorial(libro.getEditorial());
            existingLibro.setFechaAlta(libro.getFechaAlta());
            existingLibro.setFechaBaja(libro.getFechaBaja());
            existingLibro.setDepartamento(libro.getDepartamento());
            existingLibro.setFoto(libro.getFoto());
            return libroService.save(existingLibro);
        } else {
            return null; // Manejar el caso donde el libro no existe
        }
    }

    /**
     * Eliminar un libro por su ID.
     * 
     * @param id ID del libro a eliminar.
     */
    @DeleteMapping("/{id}")
    public void deleteLibro(@PathVariable int id) {
        libroService.deleteById(id);
    }
}
