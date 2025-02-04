package com.samuelcastro.ProyectoFinal.services;

import com.samuelcastro.ProyectoFinal.entities.Departamento;
import com.samuelcastro.ProyectoFinal.entities.Profesor;
import com.samuelcastro.ProyectoFinal.repositories.DepartamentoRepository;
import com.samuelcastro.ProyectoFinal.repositories.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    private static final int DEFAULT_DEPARTAMENTO_ID = 1; // ID del departamento "default"

    /**
     * Obtener todos los departamentos.
     * 
     * @return Lista de todos los departamentos.
     */
    public List<Departamento> findAll() {
        return departamentoRepository.findAll();
    }

    /**
     * Obtener un departamento por su ID.
     * 
     * @param id ID del departamento.
     * @return El departamento con el ID especificado, o null si no se encuentra.
     */
    public Departamento findById(int id) {
        return departamentoRepository.findById(id).orElse(null);
    }

    /**
     * Guardar un nuevo departamento o actualizar uno existente.
     * 
     * @param departamento Datos del departamento a guardar.
     * @return El departamento guardado.
     */
    public Departamento save(Departamento departamento) {
        return departamentoRepository.save(departamento);
    }

    /**
     * Eliminar un departamento por su ID.
     * 
     * @param id ID del departamento a eliminar.
     */
    public void deleteById(int id) {
        Departamento departamento = findById(id);
        if (departamento != null) {
            // Obtener el departamento "default"
            Departamento defaultDepartamento = departamentoRepository.findById(DEFAULT_DEPARTAMENTO_ID).orElse(null);
            if (defaultDepartamento == null) {
                throw new IllegalStateException("Default department not found");
            }

            // Reasignar profesores al departamento "default"
            List<Profesor> profesores = departamento.getProfesores();
            for (Profesor profesor : profesores) {
                profesor.setDepartamento(defaultDepartamento);
                profesorRepository.save(profesor);
            }

            // Eliminar el departamento
            departamentoRepository.deleteById(id);
        }
    }
}
