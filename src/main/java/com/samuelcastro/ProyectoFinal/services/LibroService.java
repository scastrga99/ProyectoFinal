package com.samuelcastro.ProyectoFinal.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samuelcastro.ProyectoFinal.entities.Libro;
import com.samuelcastro.ProyectoFinal.repositories.LibroRepository;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    /**
     * Obtener todos los libros.
     * 
     * @return Lista de todos los libros.
     */
    public List<Libro> findAll() {
        return libroRepository.findAll();
    }

    /**
     * Obtener un libro por su ID.
     * 
     * @param id ID del libro.
     * @return El libro con el ID especificado, o null si no se encuentra.
     */
    public Libro findById(int id) {
        return libroRepository.findById(id).orElse(null);
    }

    /**
     * Guardar un nuevo libro o actualizar uno existente.
     * 
     * @param libro Datos del libro a guardar.
     * @return El libro guardado.
     */
    public Libro save(Libro libro) {
        return libroRepository.save(libro);
    }

    /**
     * Eliminar un libro por su ID.
     * 
     * @param id ID del libro a eliminar.
     */
    public void deleteById(int id) {
        libroRepository.deleteById(id);
    }
}
