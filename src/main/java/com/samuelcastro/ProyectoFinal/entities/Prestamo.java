package com.samuelcastro.ProyectoFinal.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPrestamo;

    @ManyToOne
    @JoinColumn(name = "usuario_realiza_id", nullable = false)
    private Usuario usuarioRealiza;

    @ManyToOne
    @JoinColumn(name = "usuario_recibe_id", nullable = false)
    private Usuario usuarioRecibe;

    @ManyToOne
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fechaPrestamo;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fechaPlazo;

    @Temporal(TemporalType.DATE)
    private Date fechaDevolucion;

    @Column(nullable = false)
    private boolean devuelto;

    @PrePersist
    public void prePersist() {
        fechaPrestamo = new Date();
    }

    // Getters y setters
    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public Usuario getUsuarioRealiza() {
        return usuarioRealiza;
    }

    public void setUsuarioRealiza(Usuario usuarioRealiza) {
        this.usuarioRealiza = usuarioRealiza;
    }

    public Usuario getUsuarioRecibe() {
        return usuarioRecibe;
    }

    public void setUsuarioRecibe(Usuario usuarioRecibe) {
        this.usuarioRecibe = usuarioRecibe;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public Date getFechaPlazo() {
        return fechaPlazo;
    }

    public void setFechaPlazo(Date fechaPlazo) {
        this.fechaPlazo = fechaPlazo;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public boolean isDevuelto() {
        return devuelto;
    }

    public void setDevuelto(boolean devuelto) {
        this.devuelto = devuelto;
    }
}
