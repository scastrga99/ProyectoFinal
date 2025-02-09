package com.samuelcastro.ProyectoFinal;

import com.samuelcastro.ProyectoFinal.entities.Departamento;
import com.samuelcastro.ProyectoFinal.entities.Profesor;
import com.samuelcastro.ProyectoFinal.repositories.DepartamentoRepository;
import com.samuelcastro.ProyectoFinal.repositories.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements CommandLineRunner {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear el departamento "Sin departamento" si no existe
        if (!departamentoRepository.existsByNombre("Sin departamento")) {
            Departamento departamento = new Departamento();
            departamento.setNombre("Sin departamento");
            departamentoRepository.save(departamento);
        }

        // Crear un profesor con rol "ROLE_ADMIN" si no existe
        if (!profesorRepository.existsByRol("ROLE_ADMIN")) {
            Profesor profesor = new Profesor();
            profesor.setNombre("Samuel");
            profesor.setApellidos("Castro");
            profesor.setCorreo("samuelcastrogarciagm@gmail.com");
            profesor.setPassword(passwordEncoder.encode("admin")); // Encriptar la contraseña
            profesor.setRol("ROLE_ADMIN");
            profesor.setDepartamento(departamentoRepository.findByNombre("Sin departamento"));
            profesorRepository.save(profesor);
        }
    }
}
