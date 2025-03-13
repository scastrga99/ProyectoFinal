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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/materiales")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private DepartamentoService departamentoService;

    @GetMapping
    public String getAllDepartamentosConMateriales(Model model) {
        List<Departamento> departamentos = departamentoService.findDepartamentosConMateriales();
        if (departamentos.isEmpty()) {
            return "redirect:/api/materiales/nuevo";
        }
        model.addAttribute("departamentos", departamentos);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "materiales/departamentos-materiales";
    }

    @GetMapping("/departamento/{id}")
    public String getMaterialesPorDepartamento(@PathVariable int id, Model model) {
        List<Material> materiales = materialService.findByDepartamentoId(id);
        Map<String, List<Material>> materialesAgrupados = materiales.stream()
                .collect(Collectors.groupingBy(
                        material -> material.getNombre() + " - " + material.getMarca()
                ));
        model.addAttribute("materialesAgrupados", materialesAgrupados);
        List<Departamento> departamentos = departamentoService.findAll();
        model.addAttribute("departamentos", departamentos);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "materiales/materiales-departamento";
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

    @PostMapping("/agregar-multiples")
    public String agregarMultiplesMateriales(@RequestParam("numSeries") String numSeries,
                                             @RequestParam("nombre") String nombre,
                                             @RequestParam("marca") String marca,
                                             @RequestParam("estado") String estado,
                                             @RequestParam("departamento") int departamentoId) {
        String[] series = numSeries.split(",");
        for (String serie : series) {
            Material material = new Material();
            material.setNumSerie(serie.trim());
            material.setNombre(nombre);
            material.setMarca(marca);
            material.setEstado(estado);
            material.setFechaAlta(new Date());
            material.setDepartamento(departamentoService.findById(departamentoId));
            materialService.save(material);
        }
        return "redirect:/api/materiales";
    }

    @PostMapping("/ajustar")
    public String ajustarMaterial(@RequestParam("nombre") String nombre,
                                  @RequestParam("marca") String marca,
                                  @RequestParam("departamento") int departamentoId,
                                  @RequestParam("ajuste") String ajuste,
                                  @RequestParam("cantidad") int cantidad) {
        if (ajuste.equals("aumentar")) {
            for (int i = 0; i < cantidad; i++) {
                Material material = new Material();
                material.setNombre(nombre);
                material.setMarca(marca);
                material.setEstado("Nuevo");
                material.setFechaAlta(new Date());
                material.setNumSerie("XX");
                material.setDepartamento(departamentoService.findById(departamentoId));
                materialService.save(material);
            }
        } else if (ajuste.equals("reducir")) {
            List<Material> materiales = materialService.findByNombreAndMarca(nombre, marca);
            for (int i = 0; i < cantidad && i < materiales.size(); i++) {
                materialService.deleteById(materiales.get(i).getIdMaterial());
            }
        }
        return "redirect:/api/materiales";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarMaterial(@PathVariable int id, Model model) {
        Material material = materialService.findById(id);
        model.addAttribute("material", material);
        List<Departamento> departamentos = departamentoService.findAll();
        model.addAttribute("departamentos", departamentos);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "materiales/editar-material";
    }

    @GetMapping("/editar/{nombre}/{marca}")
    public String mostrarMaterialesAsociados(@PathVariable String nombre,
                                             @PathVariable String marca,
                                             Model model) {
        List<Material> materiales = materialService.findByNombreAndMarca(nombre, marca);
        model.addAttribute("materiales", materiales);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "materiales/materiales-asociados";
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

    private String generarNumeroDeSerieUnico() {
        String numSerie;
        do {
            numSerie = "SN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (materialService.existsByNumSerie(numSerie));
        return numSerie;
    }
}
