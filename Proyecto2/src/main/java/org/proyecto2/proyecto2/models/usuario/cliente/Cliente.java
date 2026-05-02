package org.proyecto2.proyecto2.models.usuario.cliente;

import org.apache.commons.lang3.StringUtils;
import org.proyecto2.proyecto2.dtos.usuario.cliente.ClienteRequest;
import org.proyecto2.proyecto2.dtos.usuario.cliente.ClienteUpdate;

public class Cliente {
    private int usuarioId;
    private String descripcion;
    private String sector;
    private String sitioWeb;

    public Cliente(int usuarioId, String descripcion, String sector, String sitioWeb) {
        this.usuarioId = usuarioId;
        this.descripcion = descripcion;
        this.sector = sector;
        this.sitioWeb = sitioWeb;
    }

    public Cliente(ClienteRequest clienteRequest) {
        this.usuarioId = clienteRequest.getUsuarioId();
        this.descripcion = clienteRequest.getDescripcion();
        this.sector = clienteRequest.getSector();
        this.sitioWeb = clienteRequest.getSitioWeb();
    }

    public Cliente(ClienteUpdate clienteUpdate) {
        this.usuarioId = clienteUpdate.getUsuarioId();
        this.descripcion = clienteUpdate.getDescripcion();
        this.sector = clienteUpdate.getSector();
        this.sitioWeb = clienteUpdate.getSitioWeb();
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

    public boolean isValid() {
        return usuarioId > 0
                && StringUtils.isNotBlank(descripcion)
                && StringUtils.isNotBlank(sector);
    }
}
