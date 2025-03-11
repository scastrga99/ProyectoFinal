package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.entities.Departamento;
import com.samuelcastro.ProyectoFinal.entities.Material;
import com.samuelcastro.ProyectoFinal.services.DepartamentoService;
import com.samuelcastro.ProyectoFinal.services.MaterialService;
import com.samuelcastro.ProyectoFinal.services.UsuarioDetails;
import com.samuelcastro.ProyectoFinal.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/materiales")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private DepartamentoService departamentoService;

    @GetMapping
    public String getAllMateriales(Model model) {
        List<Material> materiales = materialService.findAll();
        model.addAttribute("materiales", materiales);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "materiales/materiales";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoMaterial(Model model) {
        model.addAttribute("material", new Material());
        List<Departamento> departamentos = departamentoService.findAll();
        model.addAttribute("departamentos", departamentos);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "materiales/alta-material";
    }

    @PostMapping
    public String crearMaterial(@ModelAttribute Material material) {
        if (material.getNumSerie() == null || material.getNumSerie().isEmpty()) {
            material.setNumSerie(generarNumeroDeSerieUnico());
        }
        materialService.save(material);
        return "redirect:/api/materiales";
    }

    private String generarNumeroDeSerieUnico() {
        String numSerie;
        do {
            numSerie = "SN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (materialService.existsByNumSerie(numSerie));
        return numSerie;
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarMaterial(@PathVariable int id, Model model) {
        Material material = materialService.findById(id);
        model.addAttribute("material", material);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "materiales/editar-material";
    }

    @PostMapping("/{id}")
    public String actualizarMaterial(@PathVariable int id, @ModelAttribute Material material) {
        Material existingMaterial = materialService.findById(id);
        if (existingMaterial != null) {
            existingMaterial.setNombre(material.getNombre());
            existingMaterial.setNumSerie(material.getNumSerie());
            existingMaterial.setMarca(material.getMarca());
            existingMaterial.setDescripcion(material.getDescripcion());
            existingMaterial.setEstado(material.getEstado());
            existingMaterial.setFechaAlta(material.getFechaAlta());
            existingMaterial.setFechaBaja(material.getFechaBaja());
            existingMaterial.setDepartamento(material.getDepartamento());
            existingMaterial.setFoto(material.getFoto());
            materialService.save(existingMaterial);
        }
        return "redirect:/api/materiales";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMaterial(@PathVariable int id) {
        materialService.deleteById(id);
        return "redirect:/api/materiales";
    }
}
