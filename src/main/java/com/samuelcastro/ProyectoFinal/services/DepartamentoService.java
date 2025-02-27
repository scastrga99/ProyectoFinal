package com.samuelcastro.ProyectoFinal.services;

import com.samuelcastro.ProyectoFinal.entities.Departamento;
import com.samuelcastro.ProyectoFinal.entities.Usuario;
import com.samuelcastro.ProyectoFinal.repositories.DepartamentoRepository;
import com.samuelcastro.ProyectoFinal.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

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

            // Reasignar usuarios al departamento "default"
            List<Usuario> usuarios = departamento.getUsuarios();
            for (Usuario usuario : usuarios) {
                usuario.setDepartamento(defaultDepartamento);
                usuarioRepository.save(usuario);
            }

            // Eliminar el departamento
            departamentoRepository.deleteById(id);
        }
    }
}
