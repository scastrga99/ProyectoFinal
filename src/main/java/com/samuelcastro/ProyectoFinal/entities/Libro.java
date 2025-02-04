package com.samuelcastro.ProyectoFinal.entities;

import java.util.Date;
import jakarta.persistence.Column;
    

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idLibro;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String autor;

    @Column(nullable = false)
    private String editorial;

    @Temporal(TemporalType.DATE)
    private Date fechaAltaPrestamo;

    @Temporal(TemporalType.DATE)
    private Date fechaBajaPrestamo;

    @Column
    private String foto;

    @Column(nullable = false)
    private String estado;

    // Getters y setters
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

    public Date getFechaAltaPrestamo() {
        return fechaAltaPrestamo;
    }

    public void setFechaAltaPrestamo(Date fechaAltaPrestamo) {
        this.fechaAltaPrestamo = fechaAltaPrestamo;
    }

    public Date getFechaBajaPrestamo() {
        return fechaBajaPrestamo;
    }

    public void setFechaBajaPrestamo(Date fechaBajaPrestamo) {
        this.fechaBajaPrestamo = fechaBajaPrestamo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
