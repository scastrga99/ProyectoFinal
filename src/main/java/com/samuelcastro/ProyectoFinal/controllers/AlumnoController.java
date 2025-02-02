package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.entities.Alumno;
import com.samuelcastro.ProyectoFinal.services.AlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    /**
     * Obtener todos los alumnos.
     * 
     * @return Lista de todos los alumnos.
     */
    @GetMapping
    public List<Alumno> getAllAlumnos() {
        return alumnoService.findAll();
    }

    /**
     * Obtener un alumno por su ID.
     * 
     * @param id ID del alumno.
     * @return El alumno con el ID especificado.
     */
    @GetMapping("/{id}")
    public Alumno getAlumnoById(@PathVariable int id) {
        return alumnoService.findById(id);
    }

    /**
     * Crear un nuevo alumno.
     * 
     * @param alumno Datos del nuevo alumno.
     * @return El alumno creado.
     */
    @PostMapping
    public Alumno createAlumno(@RequestBody Alumno alumno) {
        return alumnoService.save(alumno);
    }

    /**
     * Actualizar un alumno existente.
     * 
     * @param id ID del alumno a actualizar.
     * @param alumno Datos actualizados del alumno.
     * @return El alumno actualizado.
     */
    @PutMapping("/{id}")
    public Alumno updateAlumno(@PathVariable int id, @RequestBody Alumno alumno) {
        Alumno existingAlumno = alumnoService.findById(id);
        if (existingAlumno != null) {
            existingAlumno.setNombre(alumno.getNombre());
            existingAlumno.setApellidos(alumno.getApellidos());
            existingAlumno.setCorreo(alumno.getCorreo());
            existingAlumno.setPassword(alumno.getPassword());
            existingAlumno.setFechaAlta(alumno.getFechaAlta());
            existingAlumno.setFechaBaja(alumno.getFechaBaja());
            existingAlumno.setDepartamento(alumno.getDepartamento());
            return alumnoService.save(existingAlumno);
        } else {
            return null; // Manejar el caso donde el alumno no existe
        }
    }

    /**
     * Eliminar un alumno por su ID.
     * 
     * @param id ID del alumno a eliminar.
     */
    @DeleteMapping("/{id}")
    public void deleteAlumno(@PathVariable int id) {
        alumnoService.deleteById(id);
    }
}
