package org.proyecto2.proyecto2.dtos.usuario.cliente;

import org.proyecto2.proyecto2.models.usuario.cliente.Cliente;

public class ClienteResponse extends ClienteRequest {
    public ClienteResponse(Cliente cliente) {
        this.usuarioId = cliente.getUsuarioId();
        this.descripcion = cliente.getDescripcion();
        this.sector = cliente.getSector();
        this.sitioWeb = cliente.getSitioWeb();
    }
}
