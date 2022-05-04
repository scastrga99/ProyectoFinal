package AppInventario.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the prestamo_material database table.
 * 
 */
@Entity
@Table(name="prestamo_material")
@NamedQuery(name="PrestamoMaterial.findAll", query="SELECT p FROM PrestamoMaterial p")
public class PrestamoMaterial implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id_prestamo_material;

	@Temporal(TemporalType.DATE)
	private Date fecha_devolucion;

	@Temporal(TemporalType.DATE)
	private Date fecha_prestamo;

	//bi-directional many-to-one association to Material
	@ManyToOne
	@JoinColumn(name="Id_material")
	private Material material;

	//bi-directional many-to-one association to Profesor
	@ManyToOne
	@JoinColumn(name="Id_profesor")
	private Profesor profesor;

	public PrestamoMaterial() {
	}

	public int getId_prestamo_material() {
		return this.id_prestamo_material;
	}

	public void setId_prestamo_material(int id_prestamo_material) {
		this.id_prestamo_material = id_prestamo_material;
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

	public Material getMaterial() {
		return this.material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public Profesor getProfesor() {
		return this.profesor;
	}

	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
	}

}