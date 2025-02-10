package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.entities.Profesor;
import com.samuelcastro.ProyectoFinal.services.DepartamentoService;
import com.samuelcastro.ProyectoFinal.services.ProfesorService;
import com.samuelcastro.ProyectoFinal.services.RegistroService;
import com.samuelcastro.ProyectoFinal.services.ProfesorDetails;
import com.samuelcastro.ProyectoFinal.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/profesores")
public class ProfesorController {

    @Autowired
    private ProfesorService profesorService;

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private RegistroService registroService;

    @GetMapping
    public String getAllProfesores(Model model) {
        List<Profesor> profesores = profesorService.findAll();
        model.addAttribute("profesores", profesores);
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        if (profesorDetails != null) {
            model.addAttribute("profesor", profesorDetails.getProfesor());
        }
        return "profesores/profesores";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoProfesor(Model model) {
        model.addAttribute("profesor", new Profesor());
        model.addAttribute("departamentos", departamentoService.findAll());
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        if (profesorDetails != null) {
            model.addAttribute("profesor", profesorDetails.getProfesor());
        }
        return "profesores/alta-profesor";
    }

    @PostMapping
    public String crearProfesor(@ModelAttribute Profesor profesor) {
        profesorService.save(profesor);
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        registroService.registrarOperacion("Profesor", profesor.getIdProfesor(), profesorDetails.getProfesor().getNombre() + " " + profesorDetails.getProfesor().getApellidos() + " EJECUTA CREAR ", profesor.getNombre() + " " + profesor.getApellidos());
        return "redirect:/api/profesores";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarProfesor(@PathVariable int id, Model model) {
        Profesor profesor = profesorService.findById(id);
        model.addAttribute("profesor", profesor);
        model.addAttribute("departamentos", departamentoService.findAll());
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        if (profesorDetails != null) {
            model.addAttribute("profesor", profesorDetails.getProfesor());
        }
        return "profesores/editar-profesor";
    }

    @PostMapping("/{id}")
    public String actualizarProfesor(@PathVariable int id, @ModelAttribute Profesor profesor) {
        Profesor existingProfesor = profesorService.findById(id);
        if (existingProfesor != null) {
            existingProfesor.setNombre(profesor.getNombre());
            existingProfesor.setApellidos(profesor.getApellidos());
            existingProfesor.setCorreo(profesor.getCorreo());
            existingProfesor.setPassword(profesor.getPassword());
            existingProfesor.setRol(profesor.getRol());
            existingProfesor.setDepartamento(profesor.getDepartamento());
            profesorService.save(existingProfesor);
            ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
            registroService.registrarOperacion("Profesor", existingProfesor.getIdProfesor(), profesorDetails.getProfesor().getNombre() + " " + profesorDetails.getProfesor().getApellidos() + " EJECUTA ACTUALIZAR ", existingProfesor.getNombre() + " " + existingProfesor.getApellidos());
        }
        return "redirect:/api/profesores";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProfesor(@PathVariable int id) {
        Profesor profesor = profesorService.findById(id);
        if (profesor != null) {
            profesorService.deleteById(id);
            ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
            registroService.registrarOperacion("Profesor", id, profesorDetails.getProfesor().getNombre() + " " + profesorDetails.getProfesor().getApellidos() + " EJECUTA ELIMINAR ", profesor.getNombre() + " " + profesor.getApellidos());
        }
        return "redirect:/api/profesores";
    }
}
