package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.entities.Departamento;
import com.samuelcastro.ProyectoFinal.services.DepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    @Autowired
    private DepartamentoService departamentoService;

    /**
     * Mostrar el formulario para crear un nuevo departamento.
     * 
     * @param model Modelo para pasar datos a la vista.
     * @return Nombre de la vista para crear un nuevo departamento.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoDepartamento(Model model) {
        model.addAttribute("departamento", new Departamento());
        return "departamentos/alta-departamento";
    }

    /**
     * Crear un nuevo departamento.
     * 
     * @param departamento Datos del nuevo departamento.
     * @return Redirección a la lista de departamentos.
     */
    @PostMapping
    public String crearDepartamento(Departamento departamento) {
        departamentoService.save(departamento);
        return "redirect:/api/departamentos";
    }

    /**
     * Listar todos los departamentos.
     * 
     * @param model Modelo para pasar datos a la vista.
     * @return Nombre de la vista que muestra la lista de departamentos.
     */
    @GetMapping
    public String listarDepartamentos(Model model) {
        List<Departamento> departamentos = departamentoService.findAll();
        model.addAttribute("departamentos", departamentos);
        return "departamentos/departamentos";
    }

    /**
     * Eliminar un departamento por su ID.
     * 
     * @param id ID del departamento a eliminar.
     * @return Redirección a la lista de departamentos.
     */
    @GetMapping("/delete/{id}")
    public String borrarDepartamento(@PathVariable int id) {
        departamentoService.deleteById(id);
        return "redirect:/api/departamentos";
    }

    /**
     * Mostrar el formulario para editar un departamento existente.
     * 
     * @param id ID del departamento a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return Nombre de la vista para editar el departamento.
     */
    @GetMapping("/edit/{id}")
    public String mostrarFormularioEditarDepartamento(@PathVariable int id, Model model) {
        Departamento departamento = departamentoService.findById(id);
        model.addAttribute("departamento", departamento);
        return "departamentos/editar-departamento";
    }

    /**
     * Actualizar un departamento existente.
     * 
     * @param departamento Datos actualizados del departamento.
     * @return Redirección a la lista de departamentos.
     */
    @PostMapping("/update")
    public String actualizarDepartamento(@ModelAttribute Departamento departamento) {
        departamentoService.save(departamento);
        return "redirect:/api/departamentos";
    }
}
