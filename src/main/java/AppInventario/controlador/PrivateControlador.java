package AppInventario.controlador;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import AppInventario.model.Profesor;
import AppInventario.servicio.ProfesorServicio;

@Controller
@RequestMapping("/private")
public class PrivateControlador {

	@Autowired
	private ProfesorServicio  profesorServicio;
	
	@GetMapping("/index")
	public String index(Authentication auth, HttpSession session) {
		String correo = auth.getName();
		
		if(session.getAttribute("usuario") == null) {
			Profesor usuario = profesorServicio.findByCorreo(correo); 
			usuario.setPassword(null);
			session.setAttribute("correo", usuario);
		}
		
		return "index";
	}
	
}
