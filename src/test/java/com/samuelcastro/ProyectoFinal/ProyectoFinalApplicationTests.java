package com.samuelcastro.ProyectoFinal;

import com.samuelcastro.ProyectoFinal.entities.Alumno;
import com.samuelcastro.ProyectoFinal.entities.Departamento;
import com.samuelcastro.ProyectoFinal.entities.Libro;
import com.samuelcastro.ProyectoFinal.entities.Material;
import com.samuelcastro.ProyectoFinal.entities.Profesor;
import com.samuelcastro.ProyectoFinal.repositories.AlumnoRepository;
import com.samuelcastro.ProyectoFinal.repositories.DepartamentoRepository;
import com.samuelcastro.ProyectoFinal.repositories.LibroRepository;
import com.samuelcastro.ProyectoFinal.repositories.MaterialRepository;
import com.samuelcastro.ProyectoFinal.repositories.ProfesorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class ProyectoFinalApplicationTests {

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Test
    @Transactional
    public void testCreateAndRetrieveDepartamento() {
        // Crear y guardar un departamento
        Departamento departamento = new Departamento();
        departamento.setNombre("Ciencias");
        departamento = departamentoRepository.save(departamento);

        // Verificar que el departamento fue guardado correctamente
        Departamento fetchedDepartamento = departamentoRepository.findById(departamento.getIdDepartamento()).orElse(null);
        assertThat(fetchedDepartamento).isNotNull();
        assertThat(fetchedDepartamento.getNombre()).isEqualTo("Ciencias");
    }

    @Test
    @Transactional
    public void testCreateAndRetrieveProfesor() {
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
        profesor = profesorRepository.save(profesor);

        // Verificar que el profesor fue guardado correctamente
        Profesor fetchedProfesor = profesorRepository.findById(profesor.getIdProfesor()).orElse(null);
        assertThat(fetchedProfesor).isNotNull();
        assertThat(fetchedProfesor.getNombre()).isEqualTo("Juan");
        assertThat(fetchedProfesor.getApellidos()).isEqualTo("Perez");
        assertThat(fetchedProfesor.getDepartamento()).isEqualTo(departamento);
    }

    @Test
    @Transactional
    public void testCreateAndRetrieveLibro() {
        // Crear y guardar un departamento
        Departamento departamento = new Departamento();
        departamento.setNombre("Ciencias");
        departamento = departamentoRepository.save(departamento);

        // Crear y guardar un libro
        Libro libro = new Libro();
        libro.setIsbn("1234567890");
        libro.setTitulo("Ciencia para todos");
        libro.setAutor("John Doe");
        libro.setEditorial("Editorial Ciencia");
        libro.setFechaAlta(new Date());
        libro.setFechaBaja(null);
        libro.setDepartamento(departamento);
        libro.setFoto("ciencia.jpg");
        libro = libroRepository.save(libro);

        // Verificar que el libro fue guardado correctamente
        Libro fetchedLibro = libroRepository.findById(libro.getIdLibro()).orElse(null);
        assertThat(fetchedLibro).isNotNull();
        assertThat(fetchedLibro.getIsbn()).isEqualTo("1234567890");
        assertThat(fetchedLibro.getTitulo()).isEqualTo("Ciencia para todos");
        assertThat(fetchedLibro.getDepartamento()).isEqualTo(departamento);
    }

    @Test
    @Transactional
    public void testCreateAndRetrieveAlumno() {
        // Crear y guardar un departamento
        Departamento departamento = new Departamento();
        departamento.setNombre("Ciencias");
        departamento = departamentoRepository.save(departamento);

        // Crear y guardar un alumno
        Alumno alumno = new Alumno();
        alumno.setNombre("Pedro");
        alumno.setApellidos("Gomez");
        alumno.setCorreo("pedrogomez@gmail.com");
        alumno.setFechaAlta(java.sql.Date.valueOf("2021-01-01"));
        alumno.setFechaBaja(null);
        alumno.setPassword("123456");
        alumno.setRol("estudiante");
        alumno.setDepartamento(departamento);
        alumno = alumnoRepository.save(alumno);

        // Verificar que el alumno fue guardado correctamente
        Alumno fetchedAlumno = alumnoRepository.findById(alumno.getIdAlumno()).orElse(null);
        assertThat(fetchedAlumno).isNotNull();
        assertThat(fetchedAlumno.getNombre()).isEqualTo("Pedro");
        assertThat(fetchedAlumno.getApellidos()).isEqualTo("Gomez");
        assertThat(fetchedAlumno.getDepartamento()).isEqualTo(departamento);
    }

    @Test
    @Transactional
    public void testUpdateProfesor() {
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
        profesor = profesorRepository.save(profesor);

        // Actualizar el profesor
        profesor.setNombre("Carlos");
        profesorRepository.save(profesor);

        // Verificar que el profesor fue actualizado correctamente
        Profesor updatedProfesor = profesorRepository.findById(profesor.getIdProfesor()).orElse(null);
        assertThat(updatedProfesor).isNotNull();
        assertThat(updatedProfesor.getNombre()).isEqualTo("Carlos");
    }

    @Test
    @Transactional
    public void testUpdateDepartamento() {
        // Crear y guardar un departamento
        Departamento departamento = new Departamento();
        departamento.setNombre("Ciencias");
        departamento = departamentoRepository.save(departamento);

        // Actualizar el departamento
        departamento.setNombre("Artes");
        departamentoRepository.save(departamento);

        // Verificar que el departamento fue actualizado correctamente
        Departamento updatedDepartamento = departamentoRepository.findById(departamento.getIdDepartamento()).orElse(null);
        assertThat(updatedDepartamento).isNotNull();
        assertThat(updatedDepartamento.getNombre()).isEqualTo("Artes");
    }

    @Test
    @Transactional
    public void testDeleteProfesor() {
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
        profesor = profesorRepository.save(profesor);

        // Eliminar el profesor
        profesorRepository.delete(profesor);

        // Verificar que el profesor fue eliminado correctamente
        Profesor deletedProfesor = profesorRepository.findById(profesor.getIdProfesor()).orElse(null);
        assertThat(deletedProfesor).isNull();
    }

    @Test
    @Transactional
    public void testDeleteDepartamento() {
        // Crear y guardar un departamento
        Departamento departamento = new Departamento();
        departamento.setNombre("Ciencias");
        departamento = departamentoRepository.save(departamento);

        // Eliminar el departamento
        departamentoRepository.delete(departamento);

        // Verificar que el departamento fue eliminado correctamente
        Departamento deletedDepartamento = departamentoRepository.findById(departamento.getIdDepartamento()).orElse(null);
        assertThat(deletedDepartamento).isNull();
    }

    @Test
    @Transactional
    public void testFullIntegration() {
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

        // Crear y guardar un material
        Material material = new Material();
        material.setNombre("Proyector");
        material.setNumSerie("12345");
        material.setMarca("Epson");
        material.setDescripcion("Proyector de alta definición");
        material.setFechaAlta(new Date());
        material.setFechaBaja(null);
        material.setDepartamento(departamento);
        material.setFoto("proyector.jpg");

        // Agregar el material al departamento
        departamento.addMaterial(material);

        // Guardar el material
        material = materialRepository.save(material);

        // Crear y guardar un libro
        Libro libro = new Libro();
        libro.setIsbn("1234567890");
        libro.setTitulo("Ciencia para todos");
        libro.setAutor("John Doe");
        libro.setEditorial("Editorial Ciencia");
        libro.setFechaAlta(new Date());
        libro.setFechaBaja(null);
        libro.setDepartamento(departamento);
        libro.setFoto("ciencia.jpg");

        // Agregar el libro al departamento
        departamento.addLibro(libro);

        // Guardar el libro
        libro = libroRepository.save(libro);

        // Crear y guardar un alumno
        Alumno alumno = new Alumno();
        alumno.setNombre("Pedro");
        alumno.setApellidos("Gomez");
        alumno.setCorreo("pedrogomez@gmail.com");
        alumno.setFechaAlta(java.sql.Date.valueOf("2021-01-01"));
        alumno.setFechaBaja(null);
        alumno.setPassword("123456");
        alumno.setRol("estudiante");
        alumno.setDepartamento(departamento);

        // Agregar el alumno al departamento
        departamento.addAlumno(alumno);

        // Guardar el alumno
        alumno = alumnoRepository.save(alumno);

        // Verificar que el profesor fue guardado correctamente
        Profesor fetchedProfesor = profesorRepository.findById(profesor.getIdProfesor()).orElse(null);
        assertThat(fetchedProfesor).isNotNull();
        assertThat(fetchedProfesor.getNombre()).isEqualTo("Juan");
        assertThat(fetchedProfesor.getApellidos()).isEqualTo("Perez");
        assertThat(fetchedProfesor.getDepartamento()).isEqualTo(departamento);

        // Verificar que el material fue guardado correctamente
        Material fetchedMaterial = materialRepository.findById(material.getIdMaterial()).orElse(null);
        assertThat(fetchedMaterial).isNotNull();
        assertThat(fetchedMaterial.getNombre()).isEqualTo("Proyector");
        assertThat(fetchedMaterial.getDepartamento()).isEqualTo(departamento);

        // Verificar que el libro fue guardado correctamente
        Libro fetchedLibro = libroRepository.findById(libro.getIdLibro()).orElse(null);
        assertThat(fetchedLibro).isNotNull();
        assertThat(fetchedLibro.getIsbn()).isEqualTo("1234567890");
        assertThat(fetchedLibro.getTitulo()).isEqualTo("Ciencia para todos");
        assertThat(fetchedLibro.getDepartamento()).isEqualTo(departamento);

        // Verificar que el alumno fue guardado correctamente
        Alumno fetchedAlumno = alumnoRepository.findById(alumno.getIdAlumno()).orElse(null);
        assertThat(fetchedAlumno).isNotNull();
        assertThat(fetchedAlumno.getNombre()).isEqualTo("Pedro");
        assertThat(fetchedAlumno.getApellidos()).isEqualTo("Gomez");
        assertThat(fetchedAlumno.getDepartamento()).isEqualTo(departamento);

        // Verificar que el departamento tiene el profesor en su lista de profesores
        Departamento fetchedDepartamento = departamentoRepository.findById(departamento.getIdDepartamento()).orElse(null);
        assertThat(fetchedDepartamento).isNotNull();
        assertThat(fetchedDepartamento.getProfesores()).contains(profesor);

        // Verificar que el departamento tiene el material en su lista de materiales
        assertThat(fetchedDepartamento.getMateriales()).contains(material);

        // Verificar que el departamento tiene el libro en su lista de libros
        assertThat(fetchedDepartamento.getLibros()).contains(libro);

        // Verificar que el departamento tiene el alumno en su lista de alumnos
        assertThat(fetchedDepartamento.getAlumnos()).contains(alumno);

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

        // Actualizar el material
        fetchedMaterial.setNombre("Proyector HD");
        materialRepository.save(fetchedMaterial);

        // Verificar que el material fue actualizado correctamente
        Material updatedMaterial = materialRepository.findById(fetchedMaterial.getIdMaterial()).orElse(null);
        assertThat(updatedMaterial).isNotNull();
        assertThat(updatedMaterial.getNombre()).isEqualTo("Proyector HD");

        // Actualizar el libro
        fetchedLibro.setTitulo("Ciencia avanzada");
        libroRepository.save(fetchedLibro);

        // Verificar que el libro fue actualizado correctamente
        Libro updatedLibro = libroRepository.findById(fetchedLibro.getIdLibro()).orElse(null);
        assertThat(updatedLibro).isNotNull();
        assertThat(updatedLibro.getTitulo()).isEqualTo("Ciencia avanzada");

        // Actualizar el alumno
        fetchedAlumno.setNombre("Luis");
        alumnoRepository.save(fetchedAlumno);

        // Verificar que el alumno fue actualizado correctamente
        Alumno updatedAlumno = alumnoRepository.findById(fetchedAlumno.getIdAlumno()).orElse(null);
        assertThat(updatedAlumno).isNotNull();
        assertThat(updatedAlumno.getNombre()).isEqualTo("Luis");

        // Eliminar el profesor
        profesorRepository.delete(updatedProfesor);

        // Verificar que el profesor fue eliminado correctamente
        Profesor deletedProfesor = profesorRepository.findById(updatedProfesor.getIdProfesor()).orElse(null);
        assertThat(deletedProfesor).isNull();

        // Eliminar el material
        materialRepository.delete(updatedMaterial);

        // Verificar que el material fue eliminado correctamente
        Material deletedMaterial = materialRepository.findById(updatedMaterial.getIdMaterial()).orElse(null);
        assertThat(deletedMaterial).isNull();

        // Eliminar el libro
        libroRepository.delete(updatedLibro);

        // Verificar que el libro fue eliminado correctamente
        Libro deletedLibro = libroRepository.findById(updatedLibro.getIdLibro()).orElse(null);
        assertThat(deletedLibro).isNull();

        // Eliminar el alumno
        alumnoRepository.delete(updatedAlumno);

        // Verificar que el alumno fue eliminado correctamente
        Alumno deletedAlumno = alumnoRepository.findById(updatedAlumno.getIdAlumno()).orElse(null);
        assertThat(deletedAlumno).isNull();

        // Eliminar el departamento
        departamentoRepository.delete(updatedDepartamento);

        // Verificar que el departamento fue eliminado correctamente
        Departamento deletedDepartamento = departamentoRepository.findById(updatedDepartamento.getIdDepartamento()).orElse(null);
        assertThat(deletedDepartamento).isNull();
    }
}
