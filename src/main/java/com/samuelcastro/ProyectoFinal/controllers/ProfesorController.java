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

    @GetMapping
    public List<Profesor> getAllProfesores() {
        return profesorService.findAll();
    }

    @GetMapping("/{id}")
    public Profesor getProfesorById(@PathVariable int id) {
        return profesorService.findById(id);
    }

    @GetMapping("/nombre/{nombre}")
    public List<Profesor> getProfesoresByNombre(@PathVariable String nombre) {
        return profesorService.findByNombre(nombre);
    }

    @PostMapping
    public Profesor createProfesor(@RequestBody Profesor profesor) {
        return profesorService.save(profesor);
    }

    @PutMapping("/{id}")
    public Profesor updateProfesor(@PathVariable int id, @RequestBody Profesor profesor) {
        Profesor existingProfesor = profesorService.findById(id);
        if (existingProfesor != null) {
            existingProfesor.setNombre(profesor.getNombre());
            existingProfesor.setApellidos(profesor.getApellidos());
            existingProfesor.setCorreo(profesor.getCorreo());
            existingProfesor.setPassword(profesor.getPassword());
            existingProfesor.setDepartamento(profesor.getDepartamento());
            existingProfesor.setRol(profesor.getRol());
            return profesorService.save(existingProfesor);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteProfesor(@PathVariable int id) {
        profesorService.deleteById(id);
    }
}
