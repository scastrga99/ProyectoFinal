package AppInventario.repositorio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import AppInventario.model.Libro;

@Repository
public interface LibroRepo extends CrudRepository<Libro, Integer> {

}
