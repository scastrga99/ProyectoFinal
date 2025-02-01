package com.samuelcastro.ProyectoFinal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samuelcastro.ProyectoFinal.entities.Alumno;


public interface AlumnoRepository extends JpaRepository<Alumno, Integer> {    
}
