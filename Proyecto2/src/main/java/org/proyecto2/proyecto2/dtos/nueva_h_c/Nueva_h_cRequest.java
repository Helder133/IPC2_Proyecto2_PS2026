package org.proyecto2.proyecto2.dtos.nueva_h_c;

import org.proyecto2.proyecto2.models.nueva_h_c.EnumNueva_h_cTipo;

public class Nueva_h_cRequest {
    private int usuarioId;
    private String nombre;
    private String descripcion;
    private EnumNueva_h_cTipo tipo;

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
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

    public EnumNueva_h_cTipo getTipo() {
        return tipo;
    }

    public void setTipo(EnumNueva_h_cTipo tipo) {
        this.tipo = tipo;
    }
}
