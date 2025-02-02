package com.samuelcastro.ProyectoFinal.services;

import com.samuelcastro.ProyectoFinal.entities.Material;
import com.samuelcastro.ProyectoFinal.repositories.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    /**
     * Obtener todos los materiales.
     * 
     * @return Lista de todos los materiales.
     */
    public List<Material> findAll() {
        return materialRepository.findAll();
    }

    /**
     * Obtener un material por su ID.
     * 
     * @param id ID del material.
     * @return El material con el ID especificado, o null si no se encuentra.
     */
    public Material findById(int id) {
        return materialRepository.findById(id).orElse(null);
    }

    /**
     * Guardar un nuevo material o actualizar uno existente.
     * 
     * @param material Datos del material a guardar.
     * @return El material guardado.
     */
    public Material save(Material material) {
        return materialRepository.save(material);
    }

    /**
     * Eliminar un material por su ID.
     * 
     * @param id ID del material a eliminar.
     */
    public void deleteById(int id) {
        materialRepository.deleteById(id);
    }
}
