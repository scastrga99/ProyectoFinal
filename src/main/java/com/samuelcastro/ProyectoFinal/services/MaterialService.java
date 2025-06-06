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

    public List<Material> findAll() {
        return materialRepository.findAll();
    }

    public Material findById(int id) {
        return materialRepository.findById(id).orElse(null);
    }

    public Material save(Material material) {
        return materialRepository.save(material);
    }

    public void deleteById(int id) {
        materialRepository.deleteById(id);
    }

    public boolean existsByNumSerie(String numSerie) {
        return materialRepository.existsByNumSerie(numSerie);
    }

    public List<Material> findByDepartamentoId(int departamentoId) {
        return materialRepository.findByDepartamentoId(departamentoId);
    }

    public List<Material> findByNombreAndMarca(String nombre, String marca) {
        return materialRepository.findByNombreAndMarca(nombre, marca);
    }

    public List<Material> findByNombreAndMarcaAndDepartamentoId(String nombre, String marca, int departamentoId) {
        return materialRepository.findByNombreAndMarcaAndDepartamentoId(nombre, marca, departamentoId);
    }

}
