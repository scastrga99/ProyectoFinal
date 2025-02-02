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

    /**
     * Obtener todos los materiales.
     * 
     * @return Lista de todos los materiales.
     */
    @GetMapping
    public List<Material> getAllMateriales() {
        return materialService.findAll();
    }

    /**
     * Obtener un material por su ID.
     * 
     * @param id ID del material.
     * @return El material con el ID especificado.
     */
    @GetMapping("/{id}")
    public Material getMaterialById(@PathVariable int id) {
        return materialService.findById(id);
    }

    /**
     * Crear un nuevo material.
     * 
     * @param material Datos del nuevo material.
     * @return El material creado.
     */
    @PostMapping
    public Material createMaterial(@RequestBody Material material) {
        return materialService.save(material);
    }

    /**
     * Actualizar un material existente.
     * 
     * @param id ID del material a actualizar.
     * @param material Datos actualizados del material.
     * @return El material actualizado.
     */
    @PutMapping("/{id}")
    public Material updateMaterial(@PathVariable int id, @RequestBody Material material) {
        Material existingMaterial = materialService.findById(id);
        if (existingMaterial != null) {
            existingMaterial.setNombre(material.getNombre());
            existingMaterial.setDepartamento(material.getDepartamento());
            return materialService.save(existingMaterial);
        }
        return null; // Manejar el caso donde el material no existe
    }

    /**
     * Eliminar un material por su ID.
     * 
     * @param id ID del material a eliminar.
     */
    @DeleteMapping("/{id}")
    public void deleteMaterial(@PathVariable int id) {
        materialService.deleteById(id);
    }
}
