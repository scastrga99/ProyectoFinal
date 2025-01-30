package com.samuelcastro.ProyectoFinal;

import com.samuelcastro.ProyectoFinal.entities.Profesor;
import com.samuelcastro.ProyectoFinal.repositories.ProfesorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class ProyectoFinalApplicationTests {

    @Autowired
    private ProfesorRepository profesorRepository;

    @Test
    public void testEntityProfesor() {
        // Crear y guardar un profesor
        Profesor profesor = new Profesor();
        profesor.setNombre("Juan");
        profesor.setApellidos("Perez");
        profesor.setCorreo("juanperez@gmail.com");
        profesor.setFechaAlta(java.sql.Date.valueOf("2021-01-01"));
        profesor.setFechaBaja(null);
        profesor.setPassword("123456");
        profesor.setRol("admin");
        System.out.println("Nombre: " + profesor.getNombre() + " Apellidos: " + profesor.getApellidos() + " Correo: " + profesor.getCorreo() + " Fecha de alta: " + profesor.getFechaAlta() + " Fecha de baja: " + profesor.getFechaBaja() + " Password: " + profesor.getPassword() + " Rol: " + profesor.getRol());
        profesor = profesorRepository.save(profesor);

        // Verificar que el profesor fue guardado correctamente
        Profesor fetchedProfesor = profesorRepository.findById(profesor.getIdProfesor()).orElse(null);
        assertThat(fetchedProfesor).isNotNull();
        assertThat(fetchedProfesor.getNombre()).isEqualTo("Juan");
        assertThat(fetchedProfesor.getApellidos()).isEqualTo("Perez");

        // Actualizar el profesor
        fetchedProfesor.setNombre("Carlos");
        profesorRepository.save(fetchedProfesor);

        // Verificar que el profesor fue actualizado correctamente
        Profesor updatedProfesor = profesorRepository.findById(fetchedProfesor.getIdProfesor()).orElse(null);
        assertThat(updatedProfesor).isNotNull();
        assertThat(updatedProfesor.getNombre()).isEqualTo("Carlos");

        // Eliminar el profesor
        profesorRepository.delete(updatedProfesor);

        // Verificar que el profesor fue eliminado correctamente
        Profesor deletedProfesor = profesorRepository.findById(updatedProfesor.getIdProfesor()).orElse(null);
        assertThat(deletedProfesor).isNull();
    }

    
}
