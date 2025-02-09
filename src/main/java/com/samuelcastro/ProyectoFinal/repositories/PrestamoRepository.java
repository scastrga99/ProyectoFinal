package com.samuelcastro.ProyectoFinal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.samuelcastro.ProyectoFinal.entities.Prestamo;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {

    @Query("SELECT p FROM Prestamo p WHERE p.alumno.idAlumno = :alumnoId")
    List<Prestamo> findByAlumnoId(@Param("alumnoId") int alumnoId);
}
