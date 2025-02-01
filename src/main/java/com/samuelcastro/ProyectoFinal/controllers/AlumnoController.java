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

    @GetMapping
    public List<Alumno> getAllAlumnos() {
        return alumnoService.findAll();
    }

    @GetMapping("/{id}")
    public Alumno getAlumnoById(@PathVariable int id) {
        return alumnoService.findById(id);
    }

    @PostMapping
    public Alumno createAlumno(@RequestBody Alumno alumno) {
        return alumnoService.save(alumno);
    }

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
            existingAlumno.setRol(alumno.getRol());
            return alumnoService.save(existingAlumno);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteAlumno(@PathVariable int id) {
        alumnoService.deleteById(id);
    }
}
