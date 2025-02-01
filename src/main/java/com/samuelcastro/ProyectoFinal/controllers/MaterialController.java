package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.entities.Material;
import com.samuelcastro.ProyectoFinal.services.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materiales")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @GetMapping
    public List<Material> getAllMateriales() {
        return materialService.findAll();
    }

    @GetMapping("/{id}")
    public Material getMaterialById(@PathVariable int id) {
        return materialService.findById(id);
    }

    @PostMapping
    public Material createMaterial(@RequestBody Material material) {
        return materialService.save(material);
    }

    @PutMapping("/{id}")
    public Material updateMaterial(@PathVariable int id, @RequestBody Material material) {
        Material existingMaterial = materialService.findById(id);
        if (existingMaterial != null) {
            existingMaterial.setNombre(material.getNombre());
            existingMaterial.setDepartamento(material.getDepartamento());
            return materialService.save(existingMaterial);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteMaterial(@PathVariable int id) {
        materialService.deleteById(id);
    }
}
