package com.samuelcastro.ProyectoFinal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samuelcastro.ProyectoFinal.entities.Profesor;

public interface ProfesorRepository extends JpaRepository<Profesor, Integer> {
}