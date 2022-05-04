package AppInventario.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * The persistent class for the profesor database table.
 * 
 */
@Entity
@NamedQuery(name="Profesor.findAll", query="SELECT p FROM Profesor p")
public class Profesor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id_profesor;

	private String apellidos;

	private String correo;

	@Temporal(TemporalType.DATE)
	private Date fecha_alta;

	@Temporal(TemporalType.DATE)
	private Date fecha_baja;

	private String nombre;

	private String password;

	private String rol;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "authorities_users", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "authority_id"))
	private Set<Authority> authority;

	//bi-directional many-to-one association to PrestamoLibro
	@OneToMany(mappedBy="profesor")
	private List<PrestamoLibro> prestamoLibros;

	//bi-directional many-to-one association to PrestamoMaterial
	@OneToMany(mappedBy="profesor")
	private List<PrestamoMaterial> prestamoMaterials;

	//bi-directional many-to-one association to Departamento
	@ManyToOne
	@JoinColumn(name="Departamento")
	private Departamento departamentoBean;

	public Profesor() {
	}

	public int getId_profesor() {
		return this.id_profesor;
	}

	public void setId_profesor(int id_profesor) {
		this.id_profesor = id_profesor;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
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

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRol() {
		return this.rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public List<PrestamoLibro> getPrestamoLibros() {
		return this.prestamoLibros;
	}

	public void setPrestamoLibros(List<PrestamoLibro> prestamoLibros) {
		this.prestamoLibros = prestamoLibros;
	}

	public PrestamoLibro addPrestamoLibro(PrestamoLibro prestamoLibro) {
		getPrestamoLibros().add(prestamoLibro);
		prestamoLibro.setProfesor(this);

		return prestamoLibro;
	}

	public PrestamoLibro removePrestamoLibro(PrestamoLibro prestamoLibro) {
		getPrestamoLibros().remove(prestamoLibro);
		prestamoLibro.setProfesor(null);

		return prestamoLibro;
	}

	public List<PrestamoMaterial> getPrestamoMaterials() {
		return this.prestamoMaterials;
	}

	public void setPrestamoMaterials(List<PrestamoMaterial> prestamoMaterials) {
		this.prestamoMaterials = prestamoMaterials;
	}

	public PrestamoMaterial addPrestamoMaterial(PrestamoMaterial prestamoMaterial) {
		getPrestamoMaterials().add(prestamoMaterial);
		prestamoMaterial.setProfesor(this);

		return prestamoMaterial;
	}

	public PrestamoMaterial removePrestamoMaterial(PrestamoMaterial prestamoMaterial) {
		getPrestamoMaterials().remove(prestamoMaterial);
		prestamoMaterial.setProfesor(null);

		return prestamoMaterial;
	}

	public Departamento getDepartamentoBean() {
		return this.departamentoBean;
	}

	public void setDepartamentoBean(Departamento departamentoBean) {
		this.departamentoBean = departamentoBean;
	}
	
	public Set<Authority> getAuthority() {
		return authority;
	}

	public void setAuthority(Set<Authority> authority) {
		this.authority = authority;
	}


}