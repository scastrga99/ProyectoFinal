package AppInventario.servicio;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import AppInventario.model.Libro;
import AppInventario.repositorio.LibroRepo;

@Service
public class LibroServicio{

	@Autowired
	private LibroRepo datos;
	
	
	public Iterable<Libro> listarTodo() {
		return datos.findAll();
	}


	public Optional<Libro> listarId(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
