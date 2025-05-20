package com.samuelcastro.ProyectoFinal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.samuelcastro.ProyectoFinal.entities.Prestamo;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {

    @Query("SELECT p FROM Prestamo p WHERE p.usuarioRealiza.idUsuario = :usuarioId OR p.usuarioRecibe.idUsuario = :usuarioId")
    List<Prestamo> findByUsuarioId(@Param("usuarioId") int usuarioId);

    @Query("SELECT p FROM Prestamo p WHERE p.usuarioRealiza.idUsuario = :usuarioId")
    List<Prestamo> findByUsuarioRealizaId(@Param("usuarioId") int usuarioId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Prestamo p WHERE p.libro.idLibro = :libroId")
    void deleteByLibroId(@Param("libroId") int libroId);

    @Query("SELECT p FROM Prestamo p WHERE p.libro.idLibro = :libroId")
    List<Prestamo> findByLibroId(@Param("libroId") int libroId);
}
