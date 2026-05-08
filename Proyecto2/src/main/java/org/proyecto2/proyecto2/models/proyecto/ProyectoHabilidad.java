package org.proyecto2.proyecto2.models.proyecto;

import org.proyecto2.proyecto2.dtos.proyecto.ProyectoHabilidadRequest;

public class ProyectoHabilidad {
    private int proyectoId;
    private int habilidadId;

    public ProyectoHabilidad(int proyectoId, ProyectoHabilidadRequest proyectoHabilidadRequest) {
        this.proyectoId = proyectoId;
        this.habilidadId = proyectoHabilidadRequest.getHabilidadId();
    }

    public ProyectoHabilidad(int proyectoId, int habilidadId) {
        this.proyectoId = proyectoId;
        this.habilidadId = habilidadId;
    }

    public int getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(int proyectoId) {
        this.proyectoId = proyectoId;
    }

    public int getHabilidadId() {
        return habilidadId;
    }

    public void setHabilidadId(int habilidadId) {
        this.habilidadId = habilidadId;
    }
}
