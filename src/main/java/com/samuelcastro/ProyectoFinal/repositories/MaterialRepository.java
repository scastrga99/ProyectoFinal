package com.samuelcastro.ProyectoFinal.repositories;

import com.samuelcastro.ProyectoFinal.entities.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
    boolean existsByNumSerie(String numSerie);

    @Query("SELECT m FROM Material m WHERE m.departamento.idDepartamento = :departamentoId")
    List<Material> findByDepartamentoId(@Param("departamentoId") int departamentoId);

    @Query("SELECT m FROM Material m WHERE m.nombre = :nombre AND m.marca = :marca")
    List<Material> findByNombreAndMarca(@Param("nombre") String nombre, @Param("marca") String marca);
    
    @Query("SELECT m FROM Material m WHERE m.nombre = :nombre AND m.marca = :marca AND m.departamento.idDepartamento = :departamentoId")
    List<Material> findByNombreAndMarcaAndDepartamentoId(@Param("nombre") String nombre, @Param("marca") String marca, @Param("departamentoId") int departamentoId);
}
