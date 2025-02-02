package com.samuelcastro.ProyectoFinal.services;

import com.samuelcastro.ProyectoFinal.entities.Departamento;
import com.samuelcastro.ProyectoFinal.repositories.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

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
        departamentoRepository.deleteById(id);
    }
}
