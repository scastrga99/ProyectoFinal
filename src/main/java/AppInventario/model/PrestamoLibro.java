package AppInventario.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the prestamo_libro database table.
 * 
 */
@Entity
@Table(name="prestamo_libro")
@NamedQuery(name="PrestamoLibro.findAll", query="SELECT p FROM PrestamoLibro p")
public class PrestamoLibro implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id_prestamo_libro;

	@Temporal(TemporalType.DATE)
	private Date fecha_devolucion;

	@Temporal(TemporalType.DATE)
	private Date fecha_prestamo;

	//bi-directional many-to-one association to Libro
	@ManyToOne
	@JoinColumn(name="Id_libro")
	private Libro libro;

	//bi-directional many-to-one association to Profesor
	@ManyToOne
	@JoinColumn(name="Id_profesor")
	private Profesor profesor;

	public PrestamoLibro() {
	}

	public int getId_prestamo_libro() {
		return this.id_prestamo_libro;
	}

	public void setId_prestamo_libro(int id_prestamo_libro) {
		this.id_prestamo_libro = id_prestamo_libro;
	}

	public Date getFecha_devolucion() {
		return this.fecha_devolucion;
	}

	public void setFecha_devolucion(Date fecha_devolucion) {
		this.fecha_devolucion = fecha_devolucion;
	}

	public Date getFecha_prestamo() {
		return this.fecha_prestamo;
	}

	public void setFecha_prestamo(Date fecha_prestamo) {
		this.fecha_prestamo = fecha_prestamo;
	}

	public Libro getLibro() {
		return this.libro;
	}

	public void setLibro(Libro libro) {
		this.libro = libro;
	}

	public Profesor getProfesor() {
		return this.profesor;
	}

	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
	}

}