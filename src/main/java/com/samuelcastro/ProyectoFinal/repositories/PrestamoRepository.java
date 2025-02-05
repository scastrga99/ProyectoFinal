package com.samuelcastro.ProyectoFinal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samuelcastro.ProyectoFinal.entities.Prestamo;

public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {
}
