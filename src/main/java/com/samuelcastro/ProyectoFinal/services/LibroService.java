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

    public List<Libro> findAll() {
        return libroRepository.findAll();
    }

    public Libro findById(int id) {
        return libroRepository.findById(id).orElse(null);
    }

    public Libro save(Libro Libro) {
        return libroRepository.save(Libro);
    }

    public void deleteById(int id) {
        libroRepository.deleteById(id);
    }
}
