package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.entities.Alumno;
import com.samuelcastro.ProyectoFinal.services.AlumnoService;
import com.samuelcastro.ProyectoFinal.services.DepartamentoService;
import com.samuelcastro.ProyectoFinal.services.ProfesorDetails;
import com.samuelcastro.ProyectoFinal.utils.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/alumnos")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private DepartamentoService departamentoService;

    @GetMapping
    public String getAllAlumnos(Model model) {
        List<Alumno> alumnos = alumnoService.findAll();
        model.addAttribute("alumnos", alumnos);
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        if (profesorDetails != null) {
            model.addAttribute("profesor", profesorDetails.getProfesor());
        }
        return "alumnos/alumnos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoAlumno(Model model) {
        model.addAttribute("alumno", new Alumno());
        model.addAttribute("departamentos", departamentoService.findAll());
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        if (profesorDetails != null) {
            model.addAttribute("profesor", profesorDetails.getProfesor());
        }
        return "alumnos/alta-alumno";
    }

    @PostMapping
    public String crearAlumno(@ModelAttribute Alumno alumno) {
        alumnoService.save(alumno);
        return "redirect:/api/alumnos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarAlumno(@PathVariable int id, Model model) {
        Alumno alumno = alumnoService.findById(id);
        model.addAttribute("alumno", alumno);
        model.addAttribute("departamentos", departamentoService.findAll());
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        if (profesorDetails != null) {
            model.addAttribute("profesor", profesorDetails.getProfesor());
        }
        return "alumnos/editar-alumno";
    }

    @PostMapping("/{id}")
    public String actualizarAlumno(@PathVariable int id, @ModelAttribute Alumno alumno) {
        Alumno existingAlumno = alumnoService.findById(id);
        if (existingAlumno != null) {
            existingAlumno.setNombre(alumno.getNombre());
            existingAlumno.setApellidos(alumno.getApellidos());
            existingAlumno.setCorreo(alumno.getCorreo());
            existingAlumno.setRol(alumno.getRol());
            existingAlumno.setDepartamento(alumno.getDepartamento());
            alumnoService.save(existingAlumno);
        }
        return "redirect:/api/alumnos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarAlumno(@PathVariable int id) {
        alumnoService.deleteById(id);
        return "redirect:/api/alumnos";
    }
}
