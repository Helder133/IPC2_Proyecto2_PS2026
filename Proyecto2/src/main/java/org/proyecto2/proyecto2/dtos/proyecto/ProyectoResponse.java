package org.proyecto2.proyecto2.dtos.proyecto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.proyecto2.proyecto2.dtos.categoria.CategoriaResponse;
import org.proyecto2.proyecto2.dtos.habilidad.HabilidadResponse;
import org.proyecto2.proyecto2.models.proyecto.EnumProyecto;
import org.proyecto2.proyecto2.models.proyecto.Proyecto;

import java.time.LocalDate;
import java.util.List;

public class ProyectoResponse extends ProyectoRequest {
    private int proyectoId;
    private int usuarioId;
    private int categoriaId;
    private String titulo;
    private String descripcion;
    private double presupuesto;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate fechaCreacion;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate fechaLimite;
    private List<HabilidadResponse> habilidadResponses;
    private CategoriaResponse categoria;
    private EnumProyecto estado;

    public ProyectoResponse(Proyecto proyecto) {
        this.proyectoId = proyecto.getProyectoId();
        this.usuarioId = proyecto.getUsuarioId();
        this.categoriaId = proyecto.getCategoriaId();
        this.titulo = proyecto.getTitulo();
        this.descripcion = proyecto.getDescripcion();
        this.presupuesto = proyecto.getPresupuesto();
        this.fechaCreacion = proyecto.getFechaCreacion();
        this.fechaLimite = proyecto.getFechaLimite();
        this.categoria = new CategoriaResponse(proyecto.getCategoria());
        this.habilidadResponses = proyecto.getHabilidades().stream().map(HabilidadResponse::new).toList();
        this.estado = proyecto.getEstado();
    }

    public int getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(int proyectoId) {
        this.proyectoId = proyectoId;
    }

    @Override
    public int getUsuarioId() {
        return usuarioId;
    }

    @Override
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public int getCategoriaId() {
        return categoriaId;
    }

    @Override
    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    @Override
    public String getTitulo() {
        return titulo;
    }

    @Override
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public double getPresupuesto() {
        return presupuesto;
    }

    @Override
    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    @Override
    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    @Override
    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    @Override
    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public List<HabilidadResponse> getHabilidadResponses() {
        return habilidadResponses;
    }

    public void setHabilidadResponses(List<HabilidadResponse> habilidadResponses) {
        this.habilidadResponses = habilidadResponses;
    }

    public CategoriaResponse getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaResponse categoria) {
        this.categoria = categoria;
    }

    public EnumProyecto getEstado() {
        return estado;
    }

    public void setEstado(EnumProyecto estado) {
        this.estado = estado;
    }
}
