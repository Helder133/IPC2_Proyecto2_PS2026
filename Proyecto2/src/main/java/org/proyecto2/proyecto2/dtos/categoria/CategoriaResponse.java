package org.proyecto2.proyecto2.dtos.categoria;

import org.proyecto2.proyecto2.models.categoria.Categoria;

public class CategoriaResponse extends CategoriaRequest{
    private int categoriaId;
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

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
