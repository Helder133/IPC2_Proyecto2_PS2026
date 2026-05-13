package org.proyecto2.proyecto2.dtos.usuario.cliente;

import org.proyecto2.proyecto2.models.usuario.cliente.Cliente;

public class ClienteResponse {
    private int usuarioId;
    private String descripcion;
    private String sector;
    private String sitioWeb;

    public ClienteResponse(Cliente cliente) {
        this.usuarioId = cliente.getUsuarioId();
        this.descripcion = cliente.getDescripcion();
        this.sector = cliente.getSector();
        this.sitioWeb = cliente.getSitioWeb();
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getSitioWeb() {
        return sitioWeb;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }
}
