package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.entities.Usuario;
import com.samuelcastro.ProyectoFinal.services.DepartamentoService;
import com.samuelcastro.ProyectoFinal.services.UsuarioService;
import com.samuelcastro.ProyectoFinal.services.PrestamoService;
import com.samuelcastro.ProyectoFinal.services.RegistroService;
import com.samuelcastro.ProyectoFinal.services.UsuarioDetails;
import com.samuelcastro.ProyectoFinal.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(value = "fromPerfil", required = false) String fromPerfil
    ) {
        Usuario existingUsuario = usuarioService.findById(id);
        if (existingUsuario != null) {
            existingUsuario.setNombre(usuario.getNombre());
            existingUsuario.setApellidos(usuario.getApellidos());
            existingUsuario.setCorreo(usuario.getCorreo());
            if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                existingUsuario.setPassword(usuario.getPassword());
            }
            existingUsuario.setRol(usuario.getRol());
            existingUsuario.setDepartamento(usuario.getDepartamento());
            usuarioService.save(existingUsuario);
            UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
            registroService.registrarOperacion("Usuario", existingUsuario.getIdUsuario(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA ACTUALIZAR ", existingUsuario.getNombre() + " " + existingUsuario.getApellidos());
        }
        if ("true".equals(fromPerfil)) {
            return "redirect:/api/usuarios/perfil";
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
    public String perfilUsuario(Model model) {
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "usuarios/perfil-usuario";
    }
}