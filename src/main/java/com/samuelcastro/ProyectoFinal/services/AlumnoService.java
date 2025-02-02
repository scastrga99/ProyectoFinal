package com.samuelcastro.ProyectoFinal.services;

import com.samuelcastro.ProyectoFinal.entities.Alumno;
import com.samuelcastro.ProyectoFinal.repositories.AlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    /**
     * Obtener todos los alumnos.
     * 
     * @return Lista de todos los alumnos.
     */
    public List<Alumno> findAll() {
        return alumnoRepository.findAll();
    }

    /**
     * Obtener un alumno por su ID.
     * 
     * @param id ID del alumno.
     * @return El alumno con el ID especificado, o null si no se encuentra.
     */
    public Alumno findById(int id) {
        return alumnoRepository.findById(id).orElse(null);
    }

    /**
     * Guardar un nuevo alumno o actualizar uno existente.
     * 
     * @param alumno Datos del alumno a guardar.
     * @return El alumno guardado.
     */
    public Alumno save(Alumno alumno) {
        return alumnoRepository.save(alumno);
    }

    /**
     * Eliminar un alumno por su ID.
     * 
     * @param id ID del alumno a eliminar.
     */
    public void deleteById(int id) {
        alumnoRepository.deleteById(id);
    }
}
