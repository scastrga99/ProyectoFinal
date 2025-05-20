package com.samuelcastro.ProyectoFinal.services;

import com.samuelcastro.ProyectoFinal.entities.Prestamo;
import com.samuelcastro.ProyectoFinal.entities.Libro;
import com.samuelcastro.ProyectoFinal.repositories.PrestamoRepository;
import com.samuelcastro.ProyectoFinal.repositories.LibroRepository;
import com.samuelcastro.ProyectoFinal.repositories.UsuarioRepository;
import com.samuelcastro.ProyectoFinal.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LibroRepository libroRepository;

    public List<Prestamo> findAll() {
        return prestamoRepository.findAll();
    }

    public Prestamo findById(int id) {
        return prestamoRepository.findById(id).orElse(null);
    }

    public Prestamo save(Prestamo prestamo) {
        return prestamoRepository.save(prestamo);
    }

    public void deleteById(int id) {
        prestamoRepository.deleteById(id);
    }

    public List<Prestamo> findByUsuarioId(int usuarioId) {
        return prestamoRepository.findByUsuarioId(usuarioId);
    }

    public List<Prestamo> findByUsuarioRealizaId(int usuarioId) {
        return prestamoRepository.findByUsuarioRealizaId(usuarioId);
    }

    public void deleteByLibroId(int libroId) {
        prestamoRepository.deleteByLibroId(libroId);
    }

    public void reasignarPrestamos(int usuarioId, String nombreNuevoUsuario) {
        List<Usuario> usuarios = usuarioRepository.findByNombre(nombreNuevoUsuario);
        if (usuarios.isEmpty()) {
            throw new IllegalStateException("Usuario con nombre " + nombreNuevoUsuario + " no encontrado");
        }
        Usuario nuevoUsuario = usuarios.get(0);
        if (nuevoUsuario == null) {
            throw new IllegalStateException("Usuario con nombre " + nombreNuevoUsuario + " no encontrado");
        }
        List<Prestamo> prestamos = prestamoRepository.findByUsuarioId(usuarioId);
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getUsuarioRealiza().getIdUsuario() == usuarioId) {
                prestamo.setUsuarioRealiza(nuevoUsuario);
            }
            if (prestamo.getUsuarioRecibe().getIdUsuario() == usuarioId) {
                prestamo.setUsuarioRecibe(nuevoUsuario);
            }
            prestamoRepository.save(prestamo);
        }
    }

    public void reasignarPrestamosPorLibro(int libroId, String nombreNuevoLibro) {
        List<Libro> libros = libroRepository.findByTitulo(nombreNuevoLibro);
        if (libros.isEmpty()) {
            throw new IllegalStateException("Libro con nombre " + nombreNuevoLibro + " no encontrado");
        }
        Libro nuevoLibro = libros.get(0);
        if (nuevoLibro == null) {
            throw new IllegalStateException("Libro con nombre " + nombreNuevoLibro + " no encontrado");
        }
        List<Prestamo> prestamos = prestamoRepository.findByLibroId(libroId);
        for (Prestamo prestamo : prestamos) {
            prestamo.setLibro(nuevoLibro);
            prestamoRepository.save(prestamo);
        }
    }
}