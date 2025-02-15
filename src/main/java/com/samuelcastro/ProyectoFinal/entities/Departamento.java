package com.samuelcastro.ProyectoFinal.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDepartamento;

    @Column(nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "departamento", fetch = FetchType.EAGER)
    private List<Profesor> profesores = new ArrayList<>();

    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alumno> alumnos = new ArrayList<>();

    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Material> materiales = new ArrayList<>();

    // Getters y setters
    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Profesor> getProfesores() {
        return profesores;
    }

    public void setProfesores(List<Profesor> profesores) {
        this.profesores = profesores;
    }

    public List<Material> getMateriales() {
        return materiales;
    }

    public void setMateriales(List<Material> materiales) {
        this.materiales = materiales;
    }

    public void addProfesor(Profesor profesor) {
        profesores.add(profesor);
        profesor.setDepartamento(this);
    }

    public void removeProfesor(Profesor profesor) {
        profesores.remove(profesor);
        profesor.setDepartamento(null);
    }

    public void addMaterial(Material material) {
        materiales.add(material);
        material.setDepartamento(this);
    }

    public void removeMaterial(Material material) {
        materiales.remove(material);
        material.setDepartamento(null);
    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    public void addAlumno(Alumno alumno) {
        alumnos.add(alumno);
        alumno.setDepartamento(this);
    }

}