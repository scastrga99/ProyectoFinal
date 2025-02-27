package com.samuelcastro.ProyectoFinal.repositories;

import com.samuelcastro.ProyectoFinal.entities.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Integer> {
    List<Libro> findByTituloAndAutorAndEditorial(String titulo, String autor, String editorial);
    List<Libro> findByTitulo(String titulo);
    boolean existsByTitulo(String titulo);
}