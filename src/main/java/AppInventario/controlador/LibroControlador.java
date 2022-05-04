package AppInventario.controlador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import AppInventario.model.Libro;
import AppInventario.servicio.LibroServicio;

@Controller
public class LibroControlador {

	@Autowired
	private LibroServicio service;
	
	@GetMapping("/libro")
	public String listar(Model model) {
		Iterable<Libro>libros=service.listarTodo();
		model.addAttribute("libros", libros);
		return "libro";
	}
	
}
