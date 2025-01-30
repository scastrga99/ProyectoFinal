package com.samuelcastro.ProyectoFinal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samuelcastro.ProyectoFinal.entities.Departamento;

public interface DepartamentoRepository extends JpaRepository<Departamento, Integer> {
}