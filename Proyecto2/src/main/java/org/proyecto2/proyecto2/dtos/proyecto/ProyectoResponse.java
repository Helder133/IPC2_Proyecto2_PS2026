package org.proyecto2.proyecto2.dtos.proyecto;

import org.proyecto2.proyecto2.dtos.categoria.CategoriaResponse;
import org.proyecto2.proyecto2.dtos.habilidad.HabilidadResponse;
import org.proyecto2.proyecto2.models.proyecto.Proyecto;

import java.util.ArrayList;
import java.util.List;

public class ProyectoResponse extends ProyectoRequest {
    private int proyectoId;
    private List<HabilidadResponse> habilidadResponses;
    private CategoriaResponse categoria;

    public ProyectoResponse(Proyecto proyecto) {
        this.proyectoId = proyecto.getProyectoId();
        this.usuarioId = proyecto.getUsuarioId();
        this.categoriaId = proyecto.getCategoriaId();
        this.titulo = proyecto.getTitulo();
        this.descripcion = proyecto.getDescripcion();
        this.presupuesto = proyecto.getPresupuesto();
        this.estado = proyecto.getEstado();
        this.fechaCreacion = proyecto.getFechaCreacion();
        this.fechaLimite = proyecto.getFechaLimite();
        this.categoria = new CategoriaResponse(proyecto.getCategoria());
        this.habilidadResponses = proyecto.getHabilidades().stream().map(HabilidadResponse::new).toList();
    }

    public int getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(int proyectoId) {
        this.proyectoId = proyectoId;
    }

    public List<HabilidadResponse> getHabilidadesResponse() {
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
}
