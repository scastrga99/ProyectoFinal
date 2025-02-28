package com.samuelcastro.ProyectoFinal.services;

import com.samuelcastro.ProyectoFinal.entities.Libro;
import com.samuelcastro.ProyectoFinal.repositories.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * Obtener libros por título, autor y editorial.
     * 
     * @param titulo Título del libro.
     * @param autor Autor del libro.
     * @param editorial Editorial del libro.
     * @return Lista de libros que coinciden con los criterios especificados.
     */
    public List<Libro> findByTituloAndAutorAndEditorial(String titulo, String autor, String editorial) {
        return libroRepository.findByTituloAndAutorAndEditorial(titulo, autor, editorial);
    }

    public void agregarLibroPorIsbn(String isbn, String key) {
        String[] parts = key.split(" - ");
        String titulo = parts[0];
        String autor = parts[1];
        String editorial = parts[2];
        List<Libro> libros = libroRepository.findByTituloAndAutorAndEditorial(titulo, autor, editorial);
        if (!libros.isEmpty()) {
            Libro libroExistente = libros.get(0);
            Libro nuevoLibro = new Libro();
            nuevoLibro.setIsbn(isbn);
            nuevoLibro.setTitulo(libroExistente.getTitulo());
            nuevoLibro.setAutor(libroExistente.getAutor());
            nuevoLibro.setEditorial(libroExistente.getEditorial());
            nuevoLibro.setEstado(libroExistente.getEstado());
            nuevoLibro.setFoto(libroExistente.getFoto());
            libroRepository.save(nuevoLibro);
        }
    }

    public void agregarMultiplesLibrosPorIsbn(List<String> isbnList, String key) {
        for (String isbn : isbnList) {
            agregarLibroPorIsbn(isbn, key);
        }
    }
}
