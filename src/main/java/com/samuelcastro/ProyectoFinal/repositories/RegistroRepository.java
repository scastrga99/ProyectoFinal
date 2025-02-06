package com.samuelcastro.ProyectoFinal.repositories;

import com.samuelcastro.ProyectoFinal.entities.Registro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistroRepository extends JpaRepository<Registro, Integer> {
}