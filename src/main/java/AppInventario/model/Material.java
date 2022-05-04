package AppInventario.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the material database table.
 * 
 */
@Entity
@NamedQuery(name="Material.findAll", query="SELECT m FROM Material m")
public class Material implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id_material;

	private String descripcion;

	@Temporal(TemporalType.DATE)
	private Date fecha_alta;

	@Temporal(TemporalType.DATE)
	private Date fecha_baja;

	@Lob
	private byte[] foto;

	private String marca;

	private String nombre;

	private String num_serie;

	//bi-directional many-to-one association to Departamento
	@ManyToOne
	@JoinColumn(name="Departamento")
	private Departamento departamentoBean;

	//bi-directional many-to-one association to PrestamoMaterial
	@OneToMany(mappedBy="material")
	private List<PrestamoMaterial> prestamoMaterials;

	public Material() {
	}

	public int getId_material() {
		return this.id_material;
	}

	public void setId_material(int id_material) {
		this.id_material = id_material;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFecha_alta() {
		return this.fecha_alta;
	}

	public void setFecha_alta(Date fecha_alta) {
		this.fecha_alta = fecha_alta;
	}

	public Date getFecha_baja() {
		return this.fecha_baja;
	}

	public void setFecha_baja(Date fecha_baja) {
		this.fecha_baja = fecha_baja;
	}

	public byte[] getFoto() {
		return this.foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public String getMarca() {
		return this.marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNum_serie() {
		return this.num_serie;
	}

	public void setNum_serie(String num_serie) {
		this.num_serie = num_serie;
	}

	public Departamento getDepartamentoBean() {
		return this.departamentoBean;
	}

	public void setDepartamentoBean(Departamento departamentoBean) {
		this.departamentoBean = departamentoBean;
	}

	public List<PrestamoMaterial> getPrestamoMaterials() {
		return this.prestamoMaterials;
	}

	public void setPrestamoMaterials(List<PrestamoMaterial> prestamoMaterials) {
		this.prestamoMaterials = prestamoMaterials;
	}

	public PrestamoMaterial addPrestamoMaterial(PrestamoMaterial prestamoMaterial) {
		getPrestamoMaterials().add(prestamoMaterial);
		prestamoMaterial.setMaterial(this);

		return prestamoMaterial;
	}

	public PrestamoMaterial removePrestamoMaterial(PrestamoMaterial prestamoMaterial) {
		getPrestamoMaterials().remove(prestamoMaterial);
		prestamoMaterial.setMaterial(null);

		return prestamoMaterial;
	}

}