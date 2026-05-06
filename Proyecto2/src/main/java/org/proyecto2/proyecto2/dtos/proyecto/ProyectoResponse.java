package org.proyecto2.proyecto2.dtos.proyecto;

import org.proyecto2.proyecto2.models.proyecto.Proyecto;

public class ProyectoResponse extends ProyectoRequest{
    private int proyectoId;

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
    }

    public int getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(int proyectoId) {
        this.proyectoId = proyectoId;
    }
}
