package com.samuelcastro.ProyectoFinal.entities;



import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idLibro;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String titulo;

    private String autor;

    @Column(nullable = false)
    private String editorial;

    @Column(nullable = false)
    private Date fechaAlta;

    private Date fechaBaja;

    @ManyToOne
    @JoinColumn(name = "departamento")
    private Departamento departamento;

    private String foto;

    @ManyToOne
    @JoinColumn(name = "ubicacion")
    private Dependencia ubicacion;

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(java.util.Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Dependencia getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Dependencia ubicacion) {
        this.ubicacion = ubicacion;
    }

    // Getters and setters

    
}