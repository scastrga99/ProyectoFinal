package AppInventario.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AppInventario.model.Profesor;

@Repository
public interface ProfesorRepo extends JpaRepository<Profesor, Integer> {
	public Optional<Profesor> findByUsername(String username);
}

