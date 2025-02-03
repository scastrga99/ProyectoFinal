package com.samuelcastro.ProyectoFinal.services;

import com.samuelcastro.ProyectoFinal.entities.Profesor;
import com.samuelcastro.ProyectoFinal.repositories.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Obtener todos los profesores.
     * 
     * @return Lista de todos los profesores.
     */
    public List<Profesor> findAll() {
        return profesorRepository.findAll();
    }

    /**
     * Obtener un profesor por su ID.
     * 
     * @param id ID del profesor.
     * @return El profesor con el ID especificado, o null si no se encuentra.
     */
    public Profesor findById(int id) {
        return profesorRepository.findById(id).orElse(null);
    }

    /**
     * Obtener profesores por su nombre.
     * 
     * @param nombre Nombre del profesor.
     * @return Lista de profesores con el nombre especificado.
     */
    public List<Profesor> findByNombre(String nombre) {
        return profesorRepository.findByNombre(nombre);
    }

    /**
     * Guardar un nuevo profesor o actualizar uno existente.
     * 
     * @param profesor Datos del profesor a guardar.
     * @return El profesor guardado.
     */
    public Profesor save(Profesor profesor) {
        // Codificar la contraseña antes de guardar
        profesor.setPassword(passwordEncoder.encode(profesor.getPassword()));
        return profesorRepository.save(profesor);
    }

    /**
     * Eliminar un profesor por su ID.
     * 
     * @param id ID del profesor a eliminar.
     */
    public void deleteById(int id) {
        profesorRepository.deleteById(id);
    }
}
