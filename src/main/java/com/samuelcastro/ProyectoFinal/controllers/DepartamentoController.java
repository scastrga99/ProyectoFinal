package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.entities.Departamento;
import com.samuelcastro.ProyectoFinal.services.DepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    @Autowired
    private DepartamentoService departamentoService;

    @GetMapping
    public List<Departamento> getAllDepartamentos() {
        return departamentoService.findAll();
    }

    @GetMapping("/{id}")
    public Departamento getDepartamentoById(@PathVariable int id) {
        return departamentoService.findById(id);
    }

    @PostMapping
    public Departamento createDepartamento(@RequestBody Departamento departamento) {
        return departamentoService.save(departamento);
    }

    @PutMapping("/{id}")
    public Departamento updateDepartamento(@PathVariable int id, @RequestBody Departamento departamento) {
        Departamento existingDepartamento = departamentoService.findById(id);
        if (existingDepartamento != null) {
            existingDepartamento.setNombre(departamento.getNombre());
            // Actualiza otros campos según sea necesario
            return departamentoService.save(existingDepartamento);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteDepartamento(@PathVariable int id) {
        departamentoService.deleteById(id);
    }
}
