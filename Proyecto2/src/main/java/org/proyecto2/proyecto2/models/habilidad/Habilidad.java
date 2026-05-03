package org.proyecto2.proyecto2.models.habilidad;

import org.apache.commons.lang3.StringUtils;
import org.proyecto2.proyecto2.dtos.habilidad.HabilidadRequest;
import org.proyecto2.proyecto2.dtos.habilidad.HabilidadUpdate;

public class Habilidad {
    private int habilidadId;
    private String nombre;
    private String descripcion;
    private boolean estado;

    public Habilidad(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Habilidad(HabilidadRequest  habilidadRequest) {
        this.nombre = habilidadRequest.getNombre();
        this.descripcion = habilidadRequest.getDescripcion();
    }

    public Habilidad(HabilidadUpdate habilidadUpdate) {
        this.habilidadId = habilidadUpdate.getHabilidadId();
        this.nombre = habilidadUpdate.getNombre();
        this.descripcion = habilidadUpdate.getDescripcion();
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

    public boolean isValid() {
        return StringUtils.isNotBlank(nombre)
                && StringUtils.isNotBlank(descripcion);
    }

    public boolean isValidUpdate() {
        return StringUtils.isNotBlank(nombre)
                && StringUtils.isNotBlank(descripcion)
                && habilidadId > 0;
    }
}
