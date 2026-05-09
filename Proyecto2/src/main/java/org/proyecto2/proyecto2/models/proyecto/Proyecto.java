package org.proyecto2.proyecto2.models.proyecto;

import org.apache.commons.lang3.StringUtils;
import org.proyecto2.proyecto2.dtos.proyecto.ProyectoRequest;
import org.proyecto2.proyecto2.dtos.proyecto.ProyectoUpdate;
import org.proyecto2.proyecto2.models.categoria.Categoria;
import org.proyecto2.proyecto2.models.habilidad.Habilidad;

import java.time.LocalDate;
import java.util.List;

public class Proyecto {
    private int proyectoId;
    private int usuarioId;
    private int categoriaId;
    private String titulo;
    private String descripcion;
    private double presupuesto;
    private EnumProyecto estado;
    private LocalDate fechaCreacion;
    private LocalDate fechaLimite;
    private Categoria categoria;
    private List<Habilidad>  habilidades;

    public Proyecto(int proyectoId, int usuarioId, int categoriaId, String titulo, String descripcion, double presupuesto, EnumProyecto estado, LocalDate fechaCreacion, LocalDate fechaLimite, Categoria categoria) {
        this.proyectoId = proyectoId;
        this.usuarioId = usuarioId;
        this.categoriaId = categoriaId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.presupuesto = presupuesto;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaLimite = fechaLimite;
        this.categoria = categoria;
    }

    public Proyecto(ProyectoRequest proyectoRequest) {
        this.usuarioId = proyectoRequest.getUsuarioId();
        this.categoriaId = proyectoRequest.getCategoriaId();
        this.titulo = proyectoRequest.getTitulo();
        this.descripcion = proyectoRequest.getDescripcion();
        this.presupuesto = proyectoRequest.getPresupuesto();
        this.fechaCreacion = proyectoRequest.getFechaCreacion();
        this.fechaLimite = proyectoRequest.getFechaLimite();
        this.estado = EnumProyecto.ABIERTO;
    }

    public Proyecto(ProyectoUpdate proyectoUpdate) {
        this.proyectoId = proyectoUpdate.getProyectoId();
        this.categoriaId = proyectoUpdate.getCategoriaId();
        this.titulo = proyectoUpdate.getNombre();
        this.descripcion = proyectoUpdate.getDescripcion();
        this.presupuesto = proyectoUpdate.getPresupuesto();
        this.fechaLimite = proyectoUpdate.getFechaLimite();
    }

    public int getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(int proyectoId) {
        this.proyectoId = proyectoId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public EnumProyecto getEstado() {
        return estado;
    }

    public void setEstado(EnumProyecto estado) {
        this.estado = estado;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public List<Habilidad> getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(List<Habilidad> habilidades) {
        this.habilidades = habilidades;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public boolean isValid() {
        return usuarioId > 0
                && categoriaId > 0
                && StringUtils.isNotBlank(titulo)
                && StringUtils.isNotBlank(descripcion)
                && presupuesto > 0
                && estado != null
                && fechaCreacion != null
                && fechaLimite != null
                && fechaLimite.isAfter(fechaCreacion);
    }

    public boolean isValidUpdate() {
        return proyectoId > 0
                && categoriaId > 0
                && StringUtils.isNotBlank(titulo)
                && StringUtils.isNotBlank(descripcion)
                && presupuesto > 0
                && fechaLimite != null;
    }
}
