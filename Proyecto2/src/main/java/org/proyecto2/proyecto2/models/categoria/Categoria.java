package org.proyecto2.proyecto2.models.categoria;

import org.apache.commons.lang3.StringUtils;
import org.proyecto2.proyecto2.dtos.categoria.CategoriaRequest;
import org.proyecto2.proyecto2.dtos.categoria.CategoriaUpdate;

public class Categoria {
    private int categoriaId;
    private String nombre;
    private String descripcion;
    private boolean estado;

    public Categoria(int categoriaId, String nombre, String descripcion, boolean estado) {
        this.categoriaId = categoriaId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public Categoria(CategoriaRequest categoriaRequest) {
        this.nombre = categoriaRequest.getNombre();
        this.descripcion = categoriaRequest.getDescripcion();
    }

    public Categoria(CategoriaUpdate categoriaUpdate) {
        this.categoriaId = categoriaUpdate.getCategoriaId();
        this.nombre = categoriaUpdate.getNombre();
        this.descripcion = categoriaUpdate.getDescripcion();
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
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
        return StringUtils.isNotEmpty(nombre)
                && StringUtils.isNotEmpty(descripcion);
    }

    public boolean isValidUpdate() {
        return StringUtils.isNotEmpty(nombre)
                && StringUtils.isNotEmpty(descripcion)
                && categoriaId > 0;
    }
}
