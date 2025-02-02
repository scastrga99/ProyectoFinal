package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.entities.Profesor;
import com.samuelcastro.ProyectoFinal.services.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profesores")
public class ProfesorController {

    @Autowired
    private ProfesorService profesorService;

    /**
     * Obtener todos los profesores.
     * 
     * @return Lista de todos los profesores.
     */
    @GetMapping
    public List<Profesor> getAllProfesores() {
        return profesorService.findAll();
    }

    /**
     * Obtener un profesor por su ID.
     * 
     * @param id ID del profesor.
     * @return El profesor con el ID especificado.
     */
    @GetMapping("/{id}")
    public Profesor getProfesorById(@PathVariable int id) {
        return profesorService.findById(id);
    }

    /**
     * Crear un nuevo profesor.
     * 
     * @param profesor Datos del nuevo profesor.
     * @return El profesor creado.
     */
    @PostMapping
    public Profesor createProfesor(@RequestBody Profesor profesor) {
        return profesorService.save(profesor);
    }

    /**
     * Actualizar un profesor existente.
     * 
     * @param id ID del profesor a actualizar.
     * @param profesor Datos actualizados del profesor.
     * @return El profesor actualizado.
     */
    @PutMapping("/{id}")
    public Profesor updateProfesor(@PathVariable int id, @RequestBody Profesor profesor) {
        Profesor existingProfesor = profesorService.findById(id);
        if (existingProfesor != null) {
            existingProfesor.setNombre(profesor.getNombre());
            existingProfesor.setApellidos(profesor.getApellidos());
            existingProfesor.setCorreo(profesor.getCorreo());
            existingProfesor.setPassword(profesor.getPassword());
            existingProfesor.setFechaAlta(profesor.getFechaAlta());
            existingProfesor.setFechaBaja(profesor.getFechaBaja());
            existingProfesor.setDepartamento(profesor.getDepartamento());
            return profesorService.save(existingProfesor);
        } else {
            return null; // Manejar el caso donde el profesor no existe
        }
    }

    /**
     * Eliminar un profesor por su ID.
     * 
     * @param id ID del profesor a eliminar.
     */
    @DeleteMapping("/{id}")
    public void deleteProfesor(@PathVariable int id) {
        profesorService.deleteById(id);
    }
}
