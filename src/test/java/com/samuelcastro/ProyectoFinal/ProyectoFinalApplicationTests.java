package com.samuelcastro.ProyectoFinal;

import com.samuelcastro.ProyectoFinal.entities.Departamento;
import com.samuelcastro.ProyectoFinal.entities.Profesor;
import com.samuelcastro.ProyectoFinal.repositories.DepartamentoRepository;
import com.samuelcastro.ProyectoFinal.repositories.ProfesorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class ProyectoFinalApplicationTests {

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Test
    @Transactional
    public void testEntityProfesorAndDepartamento() {
        // Crear y guardar un departamento
        Departamento departamento = new Departamento();
        departamento.setNombre("Ciencias");
        departamento = departamentoRepository.save(departamento);

        // Crear y guardar un profesor
        Profesor profesor = new Profesor();
        profesor.setNombre("Juan");
        profesor.setApellidos("Perez");
        profesor.setCorreo("juanperez@gmail.com");
        profesor.setFechaAlta(java.sql.Date.valueOf("2021-01-01"));
        profesor.setFechaBaja(null);
        profesor.setPassword("123456");
        profesor.setRol("admin");
        profesor.setDepartamento(departamento);

        // Agregar el profesor al departamento
        departamento.addProfesor(profesor);

        // Guardar el profesor
        profesor = profesorRepository.save(profesor);

        // Verificar que el profesor fue guardado correctamente
        Profesor fetchedProfesor = profesorRepository.findById(profesor.getIdProfesor()).orElse(null);
        assertThat(fetchedProfesor).isNotNull();
        assertThat(fetchedProfesor.getNombre()).isEqualTo("Juan");
        assertThat(fetchedProfesor.getApellidos()).isEqualTo("Perez");
        assertThat(fetchedProfesor.getDepartamento()).isEqualTo(departamento);

        // Verificar que el departamento tiene el profesor en su lista de profesores
        Departamento fetchedDepartamento = departamentoRepository.findById(departamento.getIdDepartamento()).orElse(null);
        assertThat(fetchedDepartamento).isNotNull();
        assertThat(fetchedDepartamento.getProfesores()).contains(profesor);

        // Actualizar el profesor
        fetchedProfesor.setNombre("Carlos");
        profesorRepository.save(fetchedProfesor);

        // Verificar que el profesor fue actualizado correctamente
        Profesor updatedProfesor = profesorRepository.findById(fetchedProfesor.getIdProfesor()).orElse(null);
        assertThat(updatedProfesor).isNotNull();
        assertThat(updatedProfesor.getNombre()).isEqualTo("Carlos");

        // Actualizar el departamento
        fetchedDepartamento.setNombre("Artes");
        departamentoRepository.save(fetchedDepartamento);

        // Verificar que el departamento fue actualizado correctamente
        Departamento updatedDepartamento = departamentoRepository.findById(fetchedDepartamento.getIdDepartamento()).orElse(null);
        assertThat(updatedDepartamento).isNotNull();
        assertThat(updatedDepartamento.getNombre()).isEqualTo("Artes");

        // Eliminar el profesor
        profesorRepository.delete(updatedProfesor);

        // Verificar que el profesor fue eliminado correctamente
        Profesor deletedProfesor = profesorRepository.findById(updatedProfesor.getIdProfesor()).orElse(null);
        assertThat(deletedProfesor).isNull();

        // Eliminar el departamento
        departamentoRepository.delete(updatedDepartamento);

        // Verificar que el departamento fue eliminado correctamente
        Departamento deletedDepartamento = departamentoRepository.findById(updatedDepartamento.getIdDepartamento()).orElse(null);
        assertThat(deletedDepartamento).isNull();
    }
}
