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

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoDepartamento(Model model) {
        model.addAttribute("departamento", new Departamento());
        return "departamentos/alta-departamento";
    }

    @PostMapping
    public String crearDepartamento(Departamento departamento) {
        departamentoService.save(departamento);
        return "redirect:/api/departamentos";
    }

    @GetMapping
    public String listarDepartamentos(Model model) {
        List<Departamento> departamentos = departamentoService.findAll();
        model.addAttribute("departamentos", departamentos);
        return "departamentos/departamentos";
    }

    @GetMapping("/delete/{id}")
    public String borrarDepartamento(@PathVariable int id) {
        departamentoService.deleteById(id);
        return "redirect:/api/departamentos";
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEditarDepartamento(@PathVariable int id, Model model) {
        Departamento departamento = departamentoService.findById(id);
        model.addAttribute("departamento", departamento);
        return "departamentos/editar-departamento";
    }

    @PostMapping("/update")
    public String actualizarDepartamento(@ModelAttribute Departamento departamento) {
        departamentoService.save(departamento);
        return "redirect:/api/departamentos";
    }
}
