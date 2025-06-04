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

    /**
     * Muestra el formulario para crear un nuevo departamento.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoDepartamento(Model model) {
        model.addAttribute("departamento", new Departamento());
        addAuthenticatedUserToModel(model);
        return "departamentos/alta-departamento";
    }

    /**
     * Procesa la creación de un nuevo departamento.
     * Si el nombre ya existe, muestra un error.
     */
    @PostMapping
    public String crearDepartamento(Departamento departamento, Model model) {
        if (departamentoService.findByNombre(departamento.getNombre()) != null) {
            model.addAttribute("departamento", departamento);
            model.addAttribute("error", "Ya existe un departamento con ese nombre.");
            addAuthenticatedUserToModel(model);
            return "departamentos/alta-departamento";
        }
        departamentoService.save(departamento);
        // Registrar la operación de creación
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        registroService.registrarOperacion("Departamento", departamento.getIdDepartamento(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA CREAR ", departamento.getNombre());
        return "redirect:/api/departamentos";
    }

    /**
     * Lista todos los departamentos.
     */
    @GetMapping
    public String listarDepartamentos(Model model) {
        List<Departamento> departamentos = departamentoService.findAll();
        model.addAttribute("departamentos", departamentos);
        addAuthenticatedUserToModel(model);
        return "departamentos/departamentos";
    }

    /**
     * Muestra el formulario para editar un departamento existente.
     */
    @GetMapping("/edit/{id}")
    public String mostrarFormularioEditarDepartamento(@PathVariable int id, Model model) {
        Departamento departamento = departamentoService.findById(id);
        model.addAttribute("departamento", departamento);
        addAuthenticatedUserToModel(model);
        return "departamentos/editar-departamento";
    }

    /**
     * Procesa la actualización de un departamento.
     */
    @PostMapping("/update")
    public String actualizarDepartamento(Departamento departamento) {
        departamentoService.save(departamento);
        // Registrar la operación de actualización
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        registroService.registrarOperacion("Departamento", departamento.getIdDepartamento(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA ACTUALIZAR ", departamento.getNombre());
        return "redirect:/api/departamentos";
    }

    /**
     * Elimina un departamento por su ID.
     */
    @GetMapping("/delete/{id}")
    public String borrarDepartamento(@PathVariable int id) {
        Departamento departamento = departamentoService.findById(id);
        if (departamento != null) {
            departamentoService.deleteById(id);
            // Registrar la operación de eliminación
            UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
            registroService.registrarOperacion("Departamento", id, usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA ELIMINAR ", departamento.getNombre());
        }
        return "redirect:/api/departamentos";
    }

    /**
     * Añade información del usuario autenticado al modelo para su uso en las vistas.
     */
    private void addAuthenticatedUserToModel(Model model) {
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
    }
}
