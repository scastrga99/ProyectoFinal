package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.entities.Usuario;
import com.samuelcastro.ProyectoFinal.services.DepartamentoService;
import com.samuelcastro.ProyectoFinal.services.EmailService;
import com.samuelcastro.ProyectoFinal.services.UsuarioService;
import com.samuelcastro.ProyectoFinal.services.PrestamoService;
import com.samuelcastro.ProyectoFinal.services.RegistroService;
import com.samuelcastro.ProyectoFinal.services.UsuarioDetails;
import com.samuelcastro.ProyectoFinal.utils.SecurityUtils;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private RegistroService registroService;

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    private static final String NOMBRE_NUEVO_USUARIO = "missingUser"; // Nombre del usuario para reasignación

    /**
     * Lista todos los usuarios y añade información del usuario autenticado al modelo.
     */
    @GetMapping
    public String getAllUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("usuarios", usuarios);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "usuarios/usuarios";
    }

    /**
     * Muestra el formulario para crear un nuevo usuario.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("departamentos", departamentoService.findAll());
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "usuarios/alta-usuario";
    }

    /**
     * Procesa la creación de un nuevo usuario.
     * Si hay error (por ejemplo, correo duplicado), muestra el error.
     */
    @PostMapping
    public String crearUsuario(@ModelAttribute Usuario usuario, Model model) {
        try {
            usuarioService.save(usuario);
        } catch (IllegalArgumentException e) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("departamentos", departamentoService.findAll());
            model.addAttribute("error", e.getMessage());
            UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
            if (usuarioDetails != null) {
                model.addAttribute("usuario", usuarioDetails.getUsuario());
                model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
            }
            return "usuarios/alta-usuario";
        }
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        registroService.registrarOperacion("Usuario", usuario.getIdUsuario(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA CREAR ", usuario.getNombre() + " " + usuario.getApellidos());
        return "redirect:/api/usuarios";
    }

    /**
     * Muestra el formulario para editar un usuario existente.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarUsuario(@PathVariable int id, Model model) {
        Usuario usuario = usuarioService.findById(id);
        model.addAttribute("usuarioActual", usuario);
        model.addAttribute("departamentos", departamentoService.findAll());
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "usuarios/editar-usuario";
    }

    /**
     * Actualiza los datos de un usuario.
     * Permite cambiar la contraseña si viene del perfil y valida la contraseña actual.
     */
    @PostMapping("/{id}")
    public String actualizarUsuario(
            @PathVariable int id,
            @ModelAttribute Usuario usuario,
            @RequestParam(value = "fromPerfil", required = false) String fromPerfil,
            @RequestParam(value = "currentPassword", required = false) String currentPassword,
            Model model
    ) {
        Usuario existingUsuario = usuarioService.findById(id);
        if (existingUsuario != null) {
            // Si viene de perfil y se quiere cambiar la contraseña
            if ("true".equals(fromPerfil) && usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                // Validar contraseña actual
                if (currentPassword == null || !passwordEncoder.matches(currentPassword, existingUsuario.getPassword())) {
                    model.addAttribute("usuario", existingUsuario);
                    model.addAttribute("volverUrl", "/"); // o la URL que corresponda
                    model.addAttribute("error", "La contraseña actual es incorrecta.");
                    return "usuarios/perfil-usuario";
                }
                existingUsuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            } else if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                // Si el admin cambia la contraseña desde editar usuario
                existingUsuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
            existingUsuario.setNombre(usuario.getNombre());
            existingUsuario.setApellidos(usuario.getApellidos());
            existingUsuario.setCorreo(usuario.getCorreo());
            existingUsuario.setRol(usuario.getRol());
            existingUsuario.setDepartamento(usuario.getDepartamento());
            usuarioService.save(existingUsuario);
            UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
            registroService.registrarOperacion("Usuario", existingUsuario.getIdUsuario(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA ACTUALIZAR ", existingUsuario.getNombre() + " " + existingUsuario.getApellidos());
        }
        if ("true".equals(fromPerfil)) {
            return "redirect:/api/usuarios/perfil?success=true";
        }
        return "redirect:/api/usuarios";
    }

    /**
     * Elimina un usuario por su ID y reasigna sus préstamos.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable int id) {
        Usuario usuario = usuarioService.findById(id);
        if (usuario != null) {
            prestamoService.reasignarPrestamos(id, NOMBRE_NUEVO_USUARIO);
            usuarioService.deleteById(id);
            UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
            registroService.registrarOperacion("Usuario", id, usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA ELIMINAR ", usuario.getNombre() + " " + usuario.getApellidos());
        }
        return "redirect:/api/usuarios";
    }

    /**
     * Muestra el formulario para alta de múltiples usuarios.
     */
    @GetMapping("/multiples")
    public String mostrarFormularioMultiplesUsuarios(Model model) {
        model.addAttribute("departamentos", departamentoService.findAll());
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "usuarios/alta-multiples-usuarios";
    }

    /**
     * Procesa la creación de múltiples usuarios desde un textarea.
     * Si algún correo ya existe, muestra los errores.
     * (NO SE ESTÁ USANDO)
     */
    @PostMapping("/multiples")
    public String crearMultiplesUsuarios(
            @RequestParam("usuariosData") String usuariosData,
            @RequestParam("departamentoId") int departamentoId,
            @RequestParam("rol") String rol,
            Model model
    ) {
        String[] lineas = usuariosData.split("\\r?\\n");
        StringBuilder errores = new StringBuilder();
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        for (String linea : lineas) {
            String[] partes = linea.split(",");
            if (partes.length >= 4) {
                String correo = partes[2].trim();
                if (usuarioService.findByCorreo(correo) != null) {
                    errores.append(correo).append("<br>");
                    continue;
                }
                Usuario usuario = new Usuario();
                usuario.setNombre(partes[0].trim());
                usuario.setApellidos(partes[1].trim());
                usuario.setCorreo(correo);
                usuario.setPassword(partes[3].trim());
                usuario.setRol(rol);
                usuario.setDepartamento(departamentoService.findById(departamentoId));
                usuarioService.save(usuario);
                if (usuarioDetails != null) {
                    registroService.registrarOperacion("Usuario", usuario.getIdUsuario(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA CREAR ", usuario.getNombre() + " " + usuario.getApellidos());
                }
            }
        }
        if (errores.length() > 0) {
            model.addAttribute("departamentos", departamentoService.findAll());
            model.addAttribute("error", "Los siguientes correos ya están registrados:<br>" + errores.toString());
            usuarioDetails = SecurityUtils.getAuthenticatedUser();
            if (usuarioDetails != null) {
                model.addAttribute("usuario", usuarioDetails.getUsuario());
                model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
            }
            return "usuarios/alta-multiples-usuarios";
        }
        return "redirect:/api/usuarios";
    }

    /**
     * Muestra el perfil del usuario autenticado.
     */
    @GetMapping("/perfil")
    public String perfilUsuario(HttpServletRequest request, Model model) {
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        String referer = request.getHeader("Referer");
        model.addAttribute("volverUrl", referer != null ? referer : "/");
        return "usuarios/perfil-usuario";
    }

    /**
     * Importa usuarios desde un archivo CSV.
     * Valida formato, roles, departamentos y correos duplicados.
     * Envía correo con contraseña generada a cada usuario importado.
     */
    @PostMapping("/importar-csv")
    public String importarUsuariosDesdeCsv(
            @RequestParam("file") MultipartFile file,
            Model model
    ) {
        if (file.isEmpty()) {
            model.addAttribute("error", "Por favor, selecciona un archivo CSV.");
            model.addAttribute("usuarios", usuarioService.findAll());
            UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
            if (usuarioDetails != null) {
                model.addAttribute("usuario", usuarioDetails.getUsuario());
                model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
            }
            return "usuarios/usuarios";
        }
        StringBuilder errores = new StringBuilder();
        List<String[]> datosUsuarios = new java.util.ArrayList<>();
        List<String[]> correosYPasswords = new java.util.ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String linea;
            int fila = 1;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length < 5) {
                    errores.append("Fila ").append(fila).append(": Formato incorrecto.<br>");
                    fila++;
                    continue;
                }
                String nombre = partes[0].trim();
                String apellidos = partes[1].trim();
                String correo = partes[2].trim();
                String rolCsv = partes[3].trim().toUpperCase();
                String departamentoNombre = partes[4].trim();

                if (nombre.length() < 2 || nombre.length() > 50) {
                    errores.append("Fila ").append(fila).append(": El nombre debe tener entre 2 y 50 caracteres.<br>");
                    fila++;
                    continue;
                }
                if (apellidos.length() < 2 || apellidos.length() > 100) {
                    errores.append("Fila ").append(fila).append(": Los apellidos deben tener entre 2 y 100 caracteres.<br>");
                    fila++;
                    continue;
                }
                if (!correo.matches("^[^\\s@]+@[^\\s@]+\\.[a-zA-Z]{2,}(?:\\.[a-zA-Z]{2,})?$")) {
                    errores.append("Fila ").append(fila).append(": Correo no válido (" + correo + ").<br>");
                    fila++;
                    continue;
                }
                String rol;
                switch (rolCsv) {
                    case "ADMIN":
                        rol = "ROLE_ADMIN";
                        break;
                    case "PROFESOR":
                        rol = "ROLE_PROFESOR";
                        break;
                    case "USUARIO":
                        rol = "ROLE_USER";
                        break;
                    default:
                        errores.append("Fila ").append(fila).append(": Rol no válido (" + rolCsv + ").<br>");
                        fila++;
                        continue;
                }
                if (usuarioService.findByCorreo(correo) != null) {
                    errores.append("Fila ").append(fila).append(": El correo ya existe (" + correo + ").<br>");
                    fila++;
                    continue;
                }
                var departamento = departamentoService.findByNombre(departamentoNombre);
                if (departamento == null) {
                    errores.append("Fila ").append(fila).append(": Departamento no encontrado (" + departamentoNombre + ").<br>");
                    fila++;
                    continue;
                }
                // Guardar los datos válidos para después
                datosUsuarios.add(new String[]{nombre, apellidos, correo, rol, departamentoNombre});
                fila++;
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al procesar el archivo: " + e.getMessage());
            model.addAttribute("usuarios", usuarioService.findAll());
            UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
            if (usuarioDetails != null) {
                model.addAttribute("usuario", usuarioDetails.getUsuario());
                model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
            }
            return "usuarios/usuarios";
        }

        // Si hay errores, NO guardar ni enviar correos
        if (errores.length() > 0) {
            model.addAttribute("error", errores.toString());
            model.addAttribute("usuarios", usuarioService.findAll());
            UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
            if (usuarioDetails != null) {
                model.addAttribute("usuario", usuarioDetails.getUsuario());
                model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
            }
            return "usuarios/usuarios";
        }

        // Si no hay errores, ahora sí creamos y guardamos los usuarios
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        for (String[] datos : datosUsuarios) {
            String nombre = datos[0];
            String apellidos = datos[1];
            String correo = datos[2];
            String rol = datos[3];
            String departamentoNombre = datos[4];
            var departamento = departamentoService.findByNombre(departamentoNombre);

            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setApellidos(apellidos);
            usuario.setCorreo(correo);
            usuario.setRol(rol);
            String password = java.util.UUID.randomUUID().toString().substring(0, 8);
            usuario.setPassword(password);
            usuario.setDepartamento(departamento);
            usuarioService.save(usuario);
            if (usuarioDetails != null) {
                registroService.registrarOperacion("Usuario", usuario.getIdUsuario(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA CREAR ", usuario.getNombre() + " " + usuario.getApellidos());
            }
            emailService.enviarNuevaContraseña(correo, password);
        }
        return "redirect:/api/usuarios";
    }
}