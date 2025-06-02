package com.samuelcastro.ProyectoFinal;

import com.samuelcastro.ProyectoFinal.entities.Departamento;
import com.samuelcastro.ProyectoFinal.entities.Libro;
import com.samuelcastro.ProyectoFinal.entities.Usuario;
import com.samuelcastro.ProyectoFinal.repositories.DepartamentoRepository;
import com.samuelcastro.ProyectoFinal.repositories.LibroRepository;
import com.samuelcastro.ProyectoFinal.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements CommandLineRunner {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear el departamento "Sin departamento" si no existe
        if (!departamentoRepository.existsByNombre("Sin departamento")) {
            Departamento departamento = new Departamento();
            departamento.setNombre("Sin departamento");
            departamentoRepository.save(departamento);
        }

        // Crear un usuario con rol "ROLE_ADMIN" si no existe
        if (!usuarioRepository.existsByRol("ROLE_ADMIN")) {
            Usuario usuario = new Usuario();
            usuario.setNombre("Samuel");
            usuario.setApellidos("Castro");
            usuario.setCorreo("samuelcastrogarciagm@gmail.com");
            usuario.setPassword(passwordEncoder.encode("admin")); // Encriptar la contraseña
            usuario.setRol("ROLE_ADMIN");
            usuario.setDepartamento(departamentoRepository.findByNombre("Sin departamento"));
            usuarioRepository.save(usuario);
        }

        // Crear un usuario con rol "ROLE_DATOS" si no existe
        if (!usuarioRepository.existsByRol("ROLE_DATOS")) {
            Usuario usuario = new Usuario();
            usuario.setNombre("missingUser");
            usuario.setApellidos("missingUser");
            usuario.setCorreo("missingUser@gmail.com");
            usuario.setPassword(passwordEncoder.encode("missingUser")); // Encriptar la contraseña
            usuario.setRol("ROLE_DATOS");
            usuario.setDepartamento(departamentoRepository.findByNombre("Sin departamento"));
            usuarioRepository.save(usuario);
        }

        // Crear un libro con título "missingLibro" si no existe
        if (!libroRepository.existsByTitulo("missingLibro")) {
            Libro libro = new Libro();
            libro.setTitulo("missingLibro");
            libro.setAutor("Desconocido");
            libro.setEditorial("Desconocida");
            libro.setIsbn("0000000000");
            libro.setEstado("Libre");
            libroRepository.save(libro);
        }
    }
}
