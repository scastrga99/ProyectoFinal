package com.samuelcastro.ProyectoFinal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samuelcastro.ProyectoFinal.entities.Material;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
    
}
