package org.proyecto2.proyecto2.dtos.habilidad;

import org.proyecto2.proyecto2.models.habilidad.Habilidad;

public class HabilidadResponse {
    private int habilidadId;
    private String nombre;
    private String descripcion;
    private boolean estado;

    public HabilidadResponse(Habilidad habilidad) {
        this.habilidadId = habilidad.getHabilidadId();
        this.nombre = habilidad.getNombre();
        this.descripcion = habilidad.getDescripcion();
        this.estado = habilidad.isEstado();
    }

    public int getHabilidadId() {
        return habilidadId;
    }

    public void setHabilidadId(int habilidadId) {
        this.habilidadId = habilidadId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
