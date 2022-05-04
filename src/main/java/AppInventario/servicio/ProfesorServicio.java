package AppInventario.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import AppInventario.interfaz.ProfesorInterfaz;
import AppInventario.model.Profesor;
import AppInventario.repositorio.ProfesorRepo;

@Service
public class ProfesorServicio implements ProfesorInterfaz {

	@Autowired
	private ProfesorRepo usuarioDao;
	
	@Override
	public Profesor findByCorreo(String Correo) {
		return usuarioDao.findByCorreo(Correo);
	}
	
	
}
