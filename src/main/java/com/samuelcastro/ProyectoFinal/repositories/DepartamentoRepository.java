package com.samuelcastro.ProyectoFinal.repositories;

import com.samuelcastro.ProyectoFinal.entities.Departamento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface DepartamentoRepository extends CrudRepository<Departamento, Integer> {
    boolean existsByNombre(String nombre);
    Departamento findByNombre(String nombre);
    @Query("SELECT DISTINCT d FROM Departamento d LEFT JOIN FETCH d.materiales")
    List<Departamento> findAllWithMateriales();
}