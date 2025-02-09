package com.samuelcastro.ProyectoFinal.repositories;

import com.samuelcastro.ProyectoFinal.entities.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartamentoRepository extends JpaRepository<Departamento, Integer> {
    boolean existsByNombre(String nombre);
    Departamento findByNombre(String nombre);
}