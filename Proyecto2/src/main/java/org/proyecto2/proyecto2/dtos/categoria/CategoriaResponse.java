package org.proyecto2.proyecto2.dtos.categoria;

import org.proyecto2.proyecto2.models.categoria.Categoria;

public class CategoriaResponse{
    private int categoriaId;
    private String nombre;
    private String descripcion;
    private boolean estado;

    public CategoriaResponse(Categoria categoria) {
        this.categoriaId = categoria.getCategoriaId();
        this.nombre = categoria.getNombre();
        this.descripcion = categoria.getDescripcion();
        this.estado = categoria.isEstado();
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
}
