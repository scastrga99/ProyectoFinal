package AppInventario.controlador;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import AppInventario.model.Profesor;

@Controller
@RequestMapping("/")
public class InicioControlador {
	
	@GetMapping("/auth/login")
	public String login(Model model) {
		model.addAttribute("profesor", new Profesor());
		
		return "login";
	}

}
