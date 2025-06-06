package com.samuelcastro.ProyectoFinal.controllers;

import com.samuelcastro.ProyectoFinal.editors.MultipartFileToByteArrayEditor;
import com.samuelcastro.ProyectoFinal.entities.Departamento;
import com.samuelcastro.ProyectoFinal.entities.Material;
import com.samuelcastro.ProyectoFinal.services.DepartamentoService;
import com.samuelcastro.ProyectoFinal.services.MaterialService;
import com.samuelcastro.ProyectoFinal.services.RegistroService;
import com.samuelcastro.ProyectoFinal.services.UsuarioDetails;
import com.samuelcastro.ProyectoFinal.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/materiales")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private RegistroService registroService;

    @InitBinder
    public void InitBinder(WebDataBinder binder) {
        binder.registerCustomEditor(byte[].class, new MultipartFileToByteArrayEditor());
    }

    /**
     * Lista todos los departamentos con sus materiales.
     */
    @GetMapping
    public String getAllDepartamentosConMateriales(Model model) {
        List<Departamento> departamentos = departamentoService.findAll();
        model.addAttribute("departamentos", departamentos);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "materiales/departamentos-materiales";
    }

    /**
     * Muestra los materiales de un departamento agrupados por nombre y marca.
     * Añade la URL de la primera foto disponible de cada grupo.
     */
    @GetMapping("/departamento/{id}")
    public String getMaterialesPorDepartamento(@PathVariable int id, Model model) {
        List<Material> materiales = materialService.findByDepartamentoId(id);
        Map<String, List<Material>> materialesAgrupados = materiales.stream()
                .collect(Collectors.groupingBy(
                        material -> material.getNombre() + " - " + material.getMarca()
                ));
        // Nueva lógica: obtener la URL de la primera imagen disponible de cada grupo
        Map<String, String> fotosMateriales = materialesAgrupados.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .filter(mat -> mat.getFoto() != null)
                                .findFirst()
                                .map(mat -> "/api/materiales/foto/" + mat.getIdMaterial())
                                .orElse("")
                ));
        model.addAttribute("materialesAgrupados", materialesAgrupados);
        model.addAttribute("fotosMateriales", fotosMateriales);
        List<Departamento> departamentos = departamentoService.findAll();
        model.addAttribute("departamentos", departamentos);
        model.addAttribute("departamento", departamentoService.findById(id));
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "materiales/materiales-departamento";
    }

    /**
     * Muestra el formulario para crear un nuevo material en un departamento.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoMaterial(@RequestParam("departamentoId") int departamentoId, Model model) {
        model.addAttribute("material", new Material());
        model.addAttribute("departamentoId", departamentoId);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "materiales/alta-material";
    }

    /**
     * Procesa la creación de un nuevo material.
     * Si no se proporciona número de serie, genera uno único.
     */
    @PostMapping
    public String crearMaterial(
        @ModelAttribute Material material,
        @RequestParam("foto") MultipartFile foto,
        @RequestParam("departamentoId") int departamentoId
    ) {
        if (material.getNumSerie() == null || material.getNumSerie().isEmpty()) {
            material.setNumSerie(generarNumeroDeSerieUnico());
        }
        if (!foto.isEmpty()) {
            try {
                material.setFoto(foto.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            material.setFoto(null);
        }
        // Asigna el departamento seleccionado
        Departamento departamento = departamentoService.findById(departamentoId);
        material.setDepartamento(departamento);

        materialService.save(material);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            registroService.registrarOperacion("Material", material.getIdMaterial(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA CREAR ", material.getNombre());
        }
        return "redirect:/api/materiales/departamento/" + departamentoId;
    }

    /**
     * Agrega múltiples materiales a partir de una lista de números de serie.
     */
    @PostMapping("/agregar-multiples")
    public String agregarMultiplesMateriales(@RequestParam("numSeries") String numSeries,
                                             @RequestParam("nombre") String nombre,
                                             @RequestParam("marca") String marca,
                                             @RequestParam("estado") String estado,
                                             @RequestParam("departamento") int departamentoId) {
        String[] series = numSeries.split(",");
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        for (String serie : series) {
            Material material = new Material();
            material.setNumSerie(serie.trim());
            material.setNombre(nombre);
            material.setMarca(marca);
            material.setEstado(estado);
            material.setFechaAlta(new Date());
            material.setDepartamento(departamentoService.findById(departamentoId));
            materialService.save(material);
            if (usuarioDetails != null) {
                registroService.registrarOperacion("Material", material.getIdMaterial(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA CREAR ", material.getNombre());
            }
        }
        return "redirect:/api/materiales/departamento/" + departamentoId;
    }

    /**
     * Ajusta la cantidad de materiales (aumentar o reducir) de un tipo.
     */
    @PostMapping("/ajustar")
    public String ajustarMaterial(@RequestParam("nombre") String nombre,
                                  @RequestParam("marca") String marca,
                                  @RequestParam("departamento") int departamentoId,
                                  @RequestParam("ajuste") String ajuste,
                                  @RequestParam("cantidad") int cantidad) {
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (ajuste.equals("aumentar")) {
            // Buscar un material original como referencia
            List<Material> materiales = materialService.findByNombreAndMarca(nombre, marca);
            Material original = materiales.isEmpty() ? null : materiales.get(0);
            for (int i = 0; i < cantidad; i++) {
                Material material = new Material();
                if (original != null) {
                    material.setNombre(original.getNombre());
                    material.setMarca(original.getMarca());
                    material.setDescripcion(original.getDescripcion());
                    material.setEstado(original.getEstado());
                    material.setFechaAlta(new Date());
                    material.setFechaBaja(original.getFechaBaja());
                    material.setDepartamento(original.getDepartamento());
                    material.setFoto(original.getFoto());
                }
                // Generar un número de serie único
                material.setNumSerie(generarNumeroDeSerieUnico());
                materialService.save(material);
                if (usuarioDetails != null) {
                    registroService.registrarOperacion("Material", material.getIdMaterial(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA CREAR ", material.getNombre());
                }
            }
        } else if (ajuste.equals("reducir")) {
            List<Material> materiales = materialService.findByNombreAndMarca(nombre, marca);
            for (int i = 0; i < cantidad && i < materiales.size(); i++) {
                if (usuarioDetails != null) {
                    registroService.registrarOperacion("Material", materiales.get(i).getIdMaterial(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA ELIMINAR ", materiales.get(i).getNombre());
                }
                materialService.deleteById(materiales.get(i).getIdMaterial());
            }
        }
        return "redirect:/api/materiales/departamento/" + departamentoId;
    }

    /**
     * Muestra el formulario para editar un material específico.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarMaterial(@PathVariable int id, Model model) {
        Material material = materialService.findById(id);
        model.addAttribute("material", material);
        List<Departamento> departamentos = departamentoService.findAll();
        model.addAttribute("departamentos", departamentos);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "materiales/editar-material";
    }

    /**
     * Muestra todos los materiales asociados a un nombre y marca.
     */
    @GetMapping("/editar/{nombre}/{marca}")
    public String mostrarMaterialesAsociados(@PathVariable String nombre,
                                             @PathVariable String marca,
                                             @RequestParam("departamentoId") int departamentoId,
                                             Model model) {
        List<Material> materiales = materialService.findByNombreAndMarcaAndDepartamentoId(nombre, marca, departamentoId);
        model.addAttribute("materiales", materiales);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "materiales/materiales-lista";
    }

    /**
     * Elimina todos los materiales asociados a un nombre y marca en un departamento.
     */
    @GetMapping("/eliminar-multiples/{nombre}/{marca}")
    public String eliminarMaterialesAsociados(
            @PathVariable String nombre,
            @PathVariable String marca,
            @RequestParam("departamentoId") int departamentoId) {
        List<Material> materiales = materialService.findByNombreAndMarca(nombre, marca);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        for (Material material : materiales) {
            if (usuarioDetails != null) {
                registroService.registrarOperacion("Material", material.getIdMaterial(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA ELIMINAR ", material.getNombre());
            }
            materialService.deleteById(material.getIdMaterial());
        }
        return "redirect:/api/materiales/departamento/" + departamentoId;
    }

    /**
     * Actualiza los datos de un material.
     */
    @PostMapping("/{id}")
    public String actualizarMaterial(@PathVariable int id, @ModelAttribute Material material, @RequestParam(value = "foto", required = false) MultipartFile foto) {
        Material existingMaterial = materialService.findById(id);
        if (existingMaterial != null) {
            existingMaterial.setNombre(material.getNombre());
            existingMaterial.setNumSerie(material.getNumSerie());
            existingMaterial.setMarca(material.getMarca());
            existingMaterial.setDescripcion(material.getDescripcion());
            existingMaterial.setEstado(material.getEstado());
            existingMaterial.setFechaAlta(material.getFechaAlta());
            existingMaterial.setFechaBaja(material.getFechaBaja());
            existingMaterial.setDepartamento(material.getDepartamento());
            if (foto != null && !foto.isEmpty()) {
                try {
                    existingMaterial.setFoto(foto.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            materialService.save(existingMaterial);
            UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
            if (usuarioDetails != null) {
                registroService.registrarOperacion("Material", existingMaterial.getIdMaterial(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA ACTUALIZAR ", existingMaterial.getNombre());
            }
            // Redirigir al departamento
            int departamentoId = existingMaterial.getDepartamento().getIdDepartamento();
            return "redirect:/api/materiales/departamento/" + departamentoId + "?materialId=" + existingMaterial.getIdMaterial();
        }
        return "redirect:/api/materiales";
    }

    /**
     * Elimina un material por su ID.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarMaterial(@PathVariable int id) {
        Material material = materialService.findById(id);
        int departamentoId = (material != null && material.getDepartamento() != null)
            ? material.getDepartamento().getIdDepartamento()
            : -1;
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (material != null && usuarioDetails != null) {
            registroService.registrarOperacion("Material", material.getIdMaterial(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA ELIMINAR ", material.getNombre());
        }
        materialService.deleteById(id);
        if (departamentoId != -1) {
            return "redirect:/api/materiales/departamento/" + departamentoId;
        }
        return "redirect:/api/materiales";
    }

    /**
     * Devuelve la foto de un material como respuesta HTTP.
     */
    @GetMapping("/foto/{id}")
    public ResponseEntity<byte[]> obtenerFoto(@PathVariable int id) {
        Material material = materialService.findById(id);
        if (material != null && material.getFoto() != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .body(material.getFoto());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Lista todos los materiales.
     */
    @GetMapping("/lista")
    public String verListaMateriales(Model model) {
        List<Material> materiales = materialService.findAll();
        model.addAttribute("materiales", materiales);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "materiales/materiales-lista";
    }

    /**
     * Muestra el formulario para alta de múltiples materiales.
     */
    @GetMapping("/alta-multiples-materiales")
    public String mostrarFormularioAltaMultiplesMateriales(@RequestParam(value = "departamentoId", required = false) Integer departamentoId, Model model) {
        model.addAttribute("departamentos", departamentoService.findAll());
        model.addAttribute("departamentoId", departamentoId);
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        if (usuarioDetails != null) {
            model.addAttribute("usuario", usuarioDetails.getUsuario());
            model.addAttribute("roles", usuarioDetails.getUsuario().getRol());
        }
        return "materiales/alta-multiples-materiales";
    }

    /**
     * Exporta los materiales de un departamento a un archivo CSV.
     */
    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportarMaterialesPorDepartamento(@RequestParam("departamentoId") int departamentoId) {
        List<Material> materiales = materialService.findByDepartamentoId(departamentoId);

        StringBuilder csv = new StringBuilder();
        csv.append("ID,Nombre,Número de Serie,Marca,Descripción,Estado,Fecha Alta,Fecha Baja,Departamento\n");
        for (Material m : materiales) {
            csv.append(m.getIdMaterial()).append(",");
            csv.append("\"").append(m.getNombre() != null ? m.getNombre().replace("\"", "\"\"") : "").append("\",");
            csv.append("\"").append(m.getNumSerie() != null ? m.getNumSerie().replace("\"", "\"\"") : "").append("\",");
            csv.append("\"").append(m.getMarca() != null ? m.getMarca().replace("\"", "\"\"") : "").append("\",");
            csv.append("\"").append(m.getDescripcion() != null ? m.getDescripcion().replace("\"", "\"\"") : "").append("\",");
            csv.append("\"").append(m.getEstado() != null ? m.getEstado().replace("\"", "\"\"") : "").append("\",");
            csv.append(m.getFechaAlta() != null ? m.getFechaAlta() : "").append(",");
            csv.append(m.getFechaBaja() != null ? m.getFechaBaja() : "").append(",");
            csv.append("\"").append(m.getDepartamento() != null ? m.getDepartamento().getNombre().replace("\"", "\"\"") : "").append("\"\n");
        }

        byte[] csvBytes = csv.toString().getBytes(StandardCharsets.UTF_8);
        String filename = "materiales_departamento_" + departamentoId + ".csv";
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(csvBytes);
    }

    /**
     * Importa materiales desde un archivo CSV.
     * Valida formato, número de serie duplicado y fechas.
     */
    @PostMapping("/importar-csv")
    public String importarMaterialesDesdeCsv(
            @RequestParam("file") MultipartFile file,
            @RequestParam("departamentoId") int departamentoId,
            Model model
    ) {
        if (file.isEmpty()) {
            model.addAttribute("error", "Por favor, selecciona un archivo CSV.");
            return "redirect:/api/materiales/departamento/" + departamentoId;
        }
        StringBuilder errores = new StringBuilder();
        List<Material> materialesAInsertar = new java.util.ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
                String numSerie = partes[1].trim();
                String marca = partes[2].trim();
                String descripcion = partes.length > 3 ? partes[3].trim() : "";
                String estado = partes.length > 4 ? partes[4].trim() : "";
                String fechaAltaStr = partes.length > 5 ? partes[5].trim() : "";
                String fechaBajaStr = partes.length > 6 ? partes[6].trim() : "";

                // Generar número de serie si está vacío
                if (numSerie.isEmpty()) {
                    numSerie = generarNumeroDeSerieUnico();
                }

                if (materialService.existsByNumSerie(numSerie)) {
                    errores.append("Fila ").append(fila).append(": El número de serie ya existe (" + numSerie + ").<br>");
                    fila++;
                    continue;
                }
                Material material = new Material();
                material.setNombre(nombre);
                material.setNumSerie(numSerie);
                material.setMarca(marca);
                material.setDescripcion(descripcion);
                material.setEstado(estado);
                try {
                    if (!fechaAltaStr.isEmpty()) {
                        material.setFechaAlta(sdf.parse(fechaAltaStr));
                    }
                    if (!fechaBajaStr.isEmpty()) {
                        material.setFechaBaja(sdf.parse(fechaBajaStr));
                    }
                } catch (Exception e) {
                    errores.append("Fila ").append(fila).append(": Fecha inválida.<br>");
                    fila++;
                    continue;
                }
                material.setDepartamento(departamentoService.findById(departamentoId));
                materialesAInsertar.add(material);
                fila++;
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al procesar el archivo: " + e.getMessage());
            model.addAttribute("departamento", departamentoService.findById(departamentoId));
            model.addAttribute("materialesAgrupados", materialService.findByDepartamentoId(departamentoId));
            return "materiales/materiales-departamento";
        }
        if (errores.length() > 0) {
            model.addAttribute("error", errores.toString());
            model.addAttribute("departamento", departamentoService.findById(departamentoId));
            model.addAttribute("materialesAgrupados", materialService.findByDepartamentoId(departamentoId));
            return "materiales/materiales-departamento";
        }
        UsuarioDetails usuarioDetails = SecurityUtils.getAuthenticatedUser();
        for (Material material : materialesAInsertar) {
            materialService.save(material);
            if (usuarioDetails != null) {
                registroService.registrarOperacion("Material", material.getIdMaterial(), usuarioDetails.getUsuario().getNombre() + " " + usuarioDetails.getUsuario().getApellidos() + " EJECUTA CREAR (IMPORTAR CSV) ", material.getNombre());
            }
        }
        return "redirect:/api/materiales/departamento/" + departamentoId;
    }

    /**
     * Genera un número de serie único para un material.
     */
    private String generarNumeroDeSerieUnico() {
        String numSerie;
        do {
            numSerie = "SN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (materialService.existsByNumSerie(numSerie));
        return numSerie;
    }
}
