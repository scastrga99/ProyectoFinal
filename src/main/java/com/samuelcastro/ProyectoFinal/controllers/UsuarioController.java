package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.entities.Usuario;
import com.samuelcastro.ProyectoFinal.services.DepartamentoService;
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

    private static final String NOMBRE_NUEVO_USUARIO = "missingUser"; // Nombre del usuario para reasignación

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
                existingUsuario.setPassword(usuario.getPassword());
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

    @PostMapping("/multiples")
    public String crearMultiplesUsuarios(
            @RequestParam("usuariosData") String usuariosData,
            @RequestParam("departamentoId") int departamentoId,
            @RequestParam("rol") String rol,
            Model model
    ) {
        String[] lineas = usuariosData.split("\\r?\\n");
        StringBuilder errores = new StringBuilder();
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
            }
        }
        if (errores.length() > 0) {
            model.addAttribute("departamentos", departamentoService.findAll());
            model.addAttribute("error", "Los siguientes correos ya están registrados:<br>" + errores.toString());
            UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
            if (usuarioDetails != null) {
                model.addAttribute("usuario", usuarioDetails.getUsuario());
                model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
            }
            return "usuarios/alta-multiples-usuarios";
        }
        return "redirect:/api/usuarios";
    }

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
        List<Usuario> usuariosAInsertar = new java.util.ArrayList<>();
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
                Usuario usuario = new Usuario();
                usuario.setNombre(nombre);
                usuario.setApellidos(apellidos);
                usuario.setCorreo(correo);
                usuario.setRol(rol);
                usuario.setPassword("123456"); // Contraseña por defecto
                usuario.setDepartamento(departamento);
                usuariosAInsertar.add(usuario);
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
        // Si no hay errores, guardar todos los usuarios
        for (Usuario usuario : usuariosAInsertar) {
            usuarioService.save(usuario);
        }
        return "redirect:/api/usuarios";
    }
}