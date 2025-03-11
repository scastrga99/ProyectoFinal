package com.samuelcastro.ProyectoFinal.repositories;

import com.samuelcastro.ProyectoFinal.entities.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
    boolean existsByNumSerie(String numSerie);
}
