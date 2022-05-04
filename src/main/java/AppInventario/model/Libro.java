package AppInventario.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the libro database table.
 * 
 */
@Entity
@NamedQuery(name="Libro.findAll", query="SELECT l FROM Libro l")
public class Libro implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id_libro;

	private String autor;

	private String editorial;

	@Temporal(TemporalType.DATE)
	private Date fecha_alta;

	@Temporal(TemporalType.DATE)
	private Date fecha_baja;

	@Lob
	private byte[] foto;

	private String isbn;

	private String titulo;

	//bi-directional many-to-one association to Departamento
	@ManyToOne
	@JoinColumn(name="Departamento")
	private Departamento departamentoBean;

	//bi-directional many-to-one association to PrestamoLibro
	@OneToMany(mappedBy="libro")
	private List<PrestamoLibro> prestamoLibros;

	public Libro() {
	}

	public int getId_libro() {
		return this.id_libro;
	}

	public void setId_libro(int id_libro) {
		this.id_libro = id_libro;
	}

	public String getAutor() {
		return this.autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getEditorial() {
		return this.editorial;
	}

	public void setEditorial(String editorial) {
		this.editorial = editorial;
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

	public String getIsbn() {
		return this.isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Departamento getDepartamentoBean() {
		return this.departamentoBean;
	}

	public void setDepartamentoBean(Departamento departamentoBean) {
		this.departamentoBean = departamentoBean;
	}

	public List<PrestamoLibro> getPrestamoLibros() {
		return this.prestamoLibros;
	}

	public void setPrestamoLibros(List<PrestamoLibro> prestamoLibros) {
		this.prestamoLibros = prestamoLibros;
	}

	public PrestamoLibro addPrestamoLibro(PrestamoLibro prestamoLibro) {
		getPrestamoLibros().add(prestamoLibro);
		prestamoLibro.setLibro(this);

		return prestamoLibro;
	}

	public PrestamoLibro removePrestamoLibro(PrestamoLibro prestamoLibro) {
		getPrestamoLibros().remove(prestamoLibro);
		prestamoLibro.setLibro(null);

		return prestamoLibro;
	}

}