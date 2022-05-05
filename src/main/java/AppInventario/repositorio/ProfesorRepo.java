package AppInventario.repositorio;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import AppInventario.model.Profesor;

@Repository
public interface ProfesorRepo extends CrudRepository<Profesor, Integer> {
	public Optional<Profesor> findByCorreo(String correo);
}

