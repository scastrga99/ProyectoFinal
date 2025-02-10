package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.entities.Alumno;
import com.samuelcastro.ProyectoFinal.entities.Prestamo;
import com.samuelcastro.ProyectoFinal.services.AlumnoService;
import com.samuelcastro.ProyectoFinal.services.DepartamentoService;
import com.samuelcastro.ProyectoFinal.services.PrestamoService;
import com.samuelcastro.ProyectoFinal.services.RegistroService;
import com.samuelcastro.ProyectoFinal.services.ProfesorDetails;
import com.samuelcastro.ProyectoFinal.utils.SecurityUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/api/alumnos")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private RegistroService registroService;

    @GetMapping
    public String getAllAlumnos(Model model) {
        List<Alumno> alumnos = alumnoService.findAll();
        model.addAttribute("alumnos", alumnos);
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        if (profesorDetails != null) {
            model.addAttribute("profesor", profesorDetails.getProfesor());
        }
        return "alumnos/alumnos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoAlumno(Model model) {
        model.addAttribute("alumno", new Alumno());
        model.addAttribute("departamentos", departamentoService.findAll());
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        if (profesorDetails != null) {
            model.addAttribute("profesor", profesorDetails.getProfesor());
        }
        return "alumnos/alta-alumno";
    }

    @PostMapping
    public String crearAlumno(@ModelAttribute Alumno alumno) {
        alumno.setRol("ROLE_USER"); // Establecer el rol predeterminado
        alumnoService.save(alumno);
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        registroService.registrarOperacion("Alumno", alumno.getIdAlumno(), profesorDetails.getProfesor().getNombre() + " " + profesorDetails.getProfesor().getApellidos() + " EJECUTA CREAR ", alumno.getNombre() + " " + alumno.getApellidos());
        return "redirect:/api/alumnos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarAlumno(@PathVariable int id, Model model) {
        Alumno alumno = alumnoService.findById(id);
        model.addAttribute("alumno", alumno);
        model.addAttribute("departamentos", departamentoService.findAll());
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        if (profesorDetails != null) {
            model.addAttribute("profesor", profesorDetails.getProfesor());
        }
        return "alumnos/editar-alumno";
    }

    @PostMapping("/{id}")
    public String actualizarAlumno(@PathVariable int id, @ModelAttribute Alumno alumno) {
        Alumno existingAlumno = alumnoService.findById(id);
        if (existingAlumno != null) {
            existingAlumno.setNombre(alumno.getNombre());
            existingAlumno.setApellidos(alumno.getApellidos());
            existingAlumno.setCorreo(alumno.getCorreo());
            existingAlumno.setDepartamento(alumno.getDepartamento());
            alumnoService.save(existingAlumno);
            ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
            registroService.registrarOperacion("Alumno", existingAlumno.getIdAlumno(), profesorDetails.getProfesor().getNombre() + " " + profesorDetails.getProfesor().getApellidos() + " EJECUTA ACTUALIZAR ", existingAlumno.getNombre() + " " + existingAlumno.getApellidos());
        }
        return "redirect:/api/alumnos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarAlumno(@PathVariable int id) {
        List<Prestamo> prestamos = prestamoService.findByAlumnoId(id);
        for (Prestamo prestamo : prestamos) {
            prestamoService.deleteById(prestamo.getIdPrestamo());
        }
        Alumno alumno = alumnoService.findById(id);
        if (alumno != null) {
            alumnoService.deleteById(id);
            ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
            registroService.registrarOperacion("Alumno", id, profesorDetails.getProfesor().getNombre() + " " + profesorDetails.getProfesor().getApellidos() + " EJECUTA ELIMINAR ", alumno.getNombre() + " " + alumno.getApellidos());
        }
        return "redirect:/api/alumnos";
    }

    @GetMapping("/subir")
    public String mostrarFormularioSubirAlumnos(Model model) {
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        if (profesorDetails != null) {
            model.addAttribute("profesor", profesorDetails.getProfesor());
            if (profesorDetails.getProfesor().getRol().equals("ROLE_ADMIN")) {
                return "alumnos/subir-alumnos";
            }
        }
        return "redirect:/api/alumnos";
    }

    @PostMapping("/subir")
    public String subirAlumnos(@RequestParam("file") MultipartFile file, Model model) {
        ProfesorDetails profesorDetails = SecurityUtils.getAuthenticatedUser();
        if (profesorDetails == null || !profesorDetails.getProfesor().getRol().equals("ROLE_ADMIN")) {
            return "redirect:/api/alumnos";
        }

        if (file.isEmpty()) {
            model.addAttribute("message", "Por favor, selecciona un archivo para subir.");
            return "alumnos/subir-alumnos";
        }

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            while (rows.hasNext()) {
                Row row = rows.next();
                if (row.getRowNum() == 0) {
                    continue; // Saltar la fila de encabezado
                }

                Alumno alumno = new Alumno();
                alumno.setNombre(row.getCell(0).getStringCellValue());
                alumno.setApellidos(row.getCell(1).getStringCellValue());
                alumno.setCorreo(row.getCell(2).getStringCellValue());
                alumno.setRol("ROLE_USER"); // Establecer el rol predeterminado
                alumno.setDepartamento(departamentoService.findById((int) row.getCell(4).getNumericCellValue()));
                alumnoService.save(alumno);
                registroService.registrarOperacion("Alumno", alumno.getIdAlumno(), profesorDetails.getProfesor().getNombre() + " " + profesorDetails.getProfesor().getApellidos() + " EJECUTA CREAR ", alumno.getNombre() + " " + alumno.getApellidos());
            }
        } catch (Exception e) {
            model.addAttribute("message", "Error al procesar el archivo: " + e.getMessage());
            return "error";
        }

        return "redirect:/api/alumnos";
    }
}
