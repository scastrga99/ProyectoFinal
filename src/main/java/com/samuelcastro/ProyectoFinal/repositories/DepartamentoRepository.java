package com.samuelcastro.ProyectoFinal.repositories;

import com.samuelcastro.ProyectoFinal.entities.Departamento;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepartamentoRepository extends JpaRepository<Departamento, Integer> {
    boolean existsByNombre(String nombre);
    Departamento findByNombre(String nombre);
    @Query("SELECT d FROM Departamento d JOIN d.materiales m WHERE m IS NOT NULL")
    List<Departamento> findDepartamentosConMateriales();
    
}