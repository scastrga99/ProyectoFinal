package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.entities.Departamento;
import com.samuelcastro.ProyectoFinal.services.DepartamentoService;
import com.samuelcastro.ProyectoFinal.services.RegistroService;
import com.samuelcastro.ProyectoFinal.services.UsuarioDetails;
import com.samuelcastro.ProyectoFinal.utils.SecurityUtils;
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

    @Autowired
    private RegistroService registroService;

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoDepartamento(Model model) {
        model.addAttribute("departamento", new Departamento());
        addAuthenticatedUserToModel(model);
        return "departamentos/alta-departamento";
    }

    @PostMapping
    public String crearDepartamento(Departamento departamento) {
        departamentoService.save(departamento);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        registroService.registrarOperacion("Departamento", departamento.getIdDepartamento(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA CREAR ", departamento.getNombre());
        return "redirect:/api/departamentos";
    }

    @GetMapping
    public String listarDepartamentos(Model model) {
        List<Departamento> departamentos = departamentoService.findAll();
        model.addAttribute("departamentos", departamentos);
        addAuthenticatedUserToModel(model);
        return "departamentos/departamentos";
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEditarDepartamento(@PathVariable int id, Model model) {
        Departamento departamento = departamentoService.findById(id);
        model.addAttribute("departamento", departamento);
        addAuthenticatedUserToModel(model);
        return "departamentos/editar-departamento";
    }

    @PostMapping("/update")
    public String actualizarDepartamento(Departamento departamento) {
        departamentoService.save(departamento);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        registroService.registrarOperacion("Departamento", departamento.getIdDepartamento(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA ACTUALIZAR ", departamento.getNombre());
        return "redirect:/api/departamentos";
    }

    @GetMapping("/delete/{id}")
    public String borrarDepartamento(@PathVariable int id) {
        Departamento departamento = departamentoService.findById(id);
        if (departamento != null) {
            departamentoService.deleteById(id);
            UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
            registroService.registrarOperacion("Departamento", id, usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA ELIMINAR ", departamento.getNombre());
        }
        return "redirect:/api/departamentos";
    }

    private void addAuthenticatedUserToModel(Model model) {
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
    }
}
