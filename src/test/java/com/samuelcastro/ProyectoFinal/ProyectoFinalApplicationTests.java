package com.samuelcastro.ProyectoFinal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;





@SpringBootTest
@ActiveProfiles("test")
public class ProyectoFinalApplicationTests {


    @Test
    @Transactional
    public void testFullIntegration() {
    //     // Crear y guardar un departamento
    //     Departamento departamento = new Departamento();
    //     departamento.setNombre("Ciencias");
    //     departamento = departamentoService.save(departamento);

    //     // Crear y guardar un profesor
    //     Profesor profesor = new Profesor();
    //     profesor.setNombre("Juan");
    //     profesor.setApellidos("Perez");
    //     profesor.setCorreo("juanperez@gmail.com");
    //     profesor.setFechaAlta(java.sql.Date.valueOf("2021-01-01"));
    //     profesor.setFechaBaja(null);
    //     profesor.setPassword("123456");
    //     profesor.setRol("admin");
    //     profesor.setDepartamento(departamento);

    //     // Guardar el profesor
    //     profesor = profesorService.save(profesor);

    //     // Actualizar la lista de profesores en el departamento
    //     departamento.getProfesores().add(profesor);
    //     departamento = departamentoService.save(departamento);

    //     // Crear y guardar un material
    //     Material material = new Material();
    //     material.setNombre("Proyector");
    //     material.setFechaAlta(new Date());
    //     material.setFechaBaja(null);
    //     material.setMarca("HP");
    //     material.setDescripcion("Proyector de alta definición");
    //     material.setDepartamento(departamento);
    //     material.setNumSerie("234234234");
    //     material.setFoto("proyector.jpg");

    //     // Guardar el material
    //     material = materialService.save(material);

    //     // Actualizar la lista de materiales en el departamento
    //     departamento.getMateriales().add(material);
    //     departamento = departamentoService.save(departamento);

    //     // Crear y guardar un libro
    //     Libro libro = new Libro();
    //     libro.setIsbn("1234567890");
    //     libro.setTitulo("Ciencia para todos");
    //     libro.setAutor("John Doe");
    //     libro.setEditorial("Editorial Ciencia");
    //     libro.setFechaAlta(new Date());
    //     libro.setFechaBaja(null);
    //     libro.setDepartamento(departamento);
    //     libro.setFoto("ciencia.jpg");

    //     // Guardar el libro
    //     libro = libroService.save(libro);

    //     // Actualizar la lista de libros en el departamento
    //     departamento.getLibros().add(libro);
    //     departamento = departamentoService.save(departamento);

    //     // Crear y guardar un alumno
    //     Alumno alumno = new Alumno();
    //     alumno.setNombre("Pedro");
    //     alumno.setApellidos("Gomez");
    //     alumno.setCorreo("pedrogomez@gmail.com");
    //     alumno.setFechaAlta(java.sql.Date.valueOf("2021-01-01"));
    //     alumno.setFechaBaja(null);
    //     alumno.setPassword("123456");
    //     alumno.setRol("estudiante");
    //     alumno.setDepartamento(departamento);

    //     // Guardar el alumno
    //     alumno = alumnoService.save(alumno);

    //     // Actualizar la lista de alumnos en el departamento
    //     departamento.getAlumnos().add(alumno);
    //     departamento = departamentoService.save(departamento);

    //     // Verificar que el profesor fue guardado correctamente
    //     Profesor fetchedProfesor = profesorService.findById(profesor.getIdProfesor());
    //     assertThat(fetchedProfesor).isNotNull();
    //     assertThat(fetchedProfesor.getNombre()).isEqualTo("Juan");
    //     assertThat(fetchedProfesor.getApellidos()).isEqualTo("Perez");
    //     assertThat(fetchedProfesor.getDepartamento()).isEqualTo(departamento);

    //     // Verificar que el material fue guardado correctamente
    //     Material fetchedMaterial = materialService.findById(material.getIdMaterial());
    //     assertThat(fetchedMaterial).isNotNull();
    //     assertThat(fetchedMaterial.getNombre()).isEqualTo("Proyector");
    //     assertThat(fetchedMaterial.getMarca()).isEqualTo("HP");
    //     assertThat(fetchedMaterial.getDescripcion()).isEqualTo("Proyector de alta definición");
    //     assertThat(fetchedMaterial.getDepartamento()).isEqualTo(departamento);

    //     // Verificar que el libro fue guardado correctamente
    //     Libro fetchedLibro = libroService.findById(libro.getIdLibro());
    //     assertThat(fetchedLibro).isNotNull();
    //     assertThat(fetchedLibro.getIsbn()).isEqualTo("1234567890");
    //     assertThat(fetchedLibro.getTitulo()).isEqualTo("Ciencia para todos");
    //     assertThat(fetchedLibro.getDepartamento()).isEqualTo(departamento);

    //     // Verificar que el alumno fue guardado correctamente
    //     Alumno fetchedAlumno = alumnoService.findById(alumno.getIdAlumno());
    //     assertThat(fetchedAlumno).isNotNull();
    //     assertThat(fetchedAlumno.getNombre()).isEqualTo("Pedro");
    //     assertThat(fetchedAlumno.getApellidos()).isEqualTo("Gomez");
    //     assertThat(fetchedAlumno.getDepartamento()).isEqualTo(departamento);

    //     // Verificar que el departamento tiene el profesor en su lista de profesores
    //     Departamento fetchedDepartamento = departamentoService.findById(departamento.getIdDepartamento());
    //     assertThat(fetchedDepartamento).isNotNull();
    //     assertThat(fetchedDepartamento.getProfesores()).contains(profesor);

    //     // Verificar que el departamento tiene el material en su lista de materiales
    //     assertThat(fetchedDepartamento.getMateriales()).contains(material);

    //     // Verificar que el departamento tiene el libro en su lista de libros
    //     assertThat(fetchedDepartamento.getLibros()).contains(libro);

    //     // Verificar que el departamento tiene el alumno en su lista de alumnos
    //     assertThat(fetchedDepartamento.getAlumnos()).contains(alumno);

    //     // Actualizar el profesor
    //     fetchedProfesor.setNombre("Carlos");
    //     profesorService.save(fetchedProfesor);

    //     // Verificar que el profesor fue actualizado correctamente
    //     Profesor updatedProfesor = profesorService.findById(fetchedProfesor.getIdProfesor());
    //     assertThat(updatedProfesor).isNotNull();
    //     assertThat(updatedProfesor.getNombre()).isEqualTo("Carlos");

    //     // Actualizar el departamento
    //     fetchedDepartamento.setNombre("Artes");
    //     departamentoService.save(fetchedDepartamento);

    //     // Verificar que el departamento fue actualizado correctamente
    //     Departamento updatedDepartamento = departamentoService.findById(fetchedDepartamento.getIdDepartamento());
    //     assertThat(updatedDepartamento).isNotNull();
    //     assertThat(updatedDepartamento.getNombre()).isEqualTo("Artes");

    //     // Actualizar el material
    //     fetchedMaterial.setNombre("Proyector HD");
    //     materialService.save(fetchedMaterial);

    //     // Verificar que el material fue actualizado correctamente
    //     Material updatedMaterial = materialService.findById(fetchedMaterial.getIdMaterial());
    //     assertThat(updatedMaterial).isNotNull();
    //     assertThat(updatedMaterial.getNombre()).isEqualTo("Proyector HD");

    //     // Actualizar el libro
    //     fetchedLibro.setTitulo("Ciencia avanzada");
    //     libroService.save(fetchedLibro);

    //     // Verificar que el libro fue actualizado correctamente
    //     Libro updatedLibro = libroService.findById(fetchedLibro.getIdLibro());
    //     assertThat(updatedLibro).isNotNull();
    //     assertThat(updatedLibro.getTitulo()).isEqualTo("Ciencia avanzada");

    //     // Actualizar el alumno
    //     fetchedAlumno.setNombre("Luis");
    //     alumnoService.save(fetchedAlumno);

    //     // Verificar que el alumno fue actualizado correctamente
    //     Alumno updatedAlumno = alumnoService.findById(fetchedAlumno.getIdAlumno());
    //     assertThat(updatedAlumno).isNotNull();
    //     assertThat(updatedAlumno.getNombre()).isEqualTo("Luis");

    //     // Eliminar el profesor
    //     profesorService.deleteById(updatedProfesor.getIdProfesor());

    //     // Verificar que el profesor fue eliminado correctamente
    //     Profesor deletedProfesor = profesorService.findById(updatedProfesor.getIdProfesor());
    //     assertThat(deletedProfesor).isNull();

    //     // Eliminar el material
    //     materialService.deleteById(updatedMaterial.getIdMaterial());

    //     // Verificar que el material fue eliminado correctamente
    //     Material deletedMaterial = materialService.findById(updatedMaterial.getIdMaterial());
    //     assertThat(deletedMaterial).isNull();

    //     // Eliminar el libro
    //     libroService.deleteById(updatedLibro.getIdLibro());

    //     // Verificar que el libro fue eliminado correctamente
    //     Libro deletedLibro = libroService.findById(updatedLibro.getIdLibro());
    //     assertThat(deletedLibro).isNull();

    //     // Eliminar el alumno
    //     alumnoService.deleteById(updatedAlumno.getIdAlumno());

    //     // Verificar que el alumno fue eliminado correctamente
    //     Alumno deletedAlumno = alumnoService.findById(updatedAlumno.getIdAlumno());
    //     assertThat(deletedAlumno).isNull();

    //     // Eliminar el departamento
    //     departamentoService.deleteById(updatedDepartamento.getIdDepartamento());

    //     // Verificar que el departamento fue eliminado correctamente
    //     Departamento deletedDepartamento = departamentoService.findById(updatedDepartamento.getIdDepartamento());
    //     assertThat(deletedDepartamento).isNull();
    }
}
