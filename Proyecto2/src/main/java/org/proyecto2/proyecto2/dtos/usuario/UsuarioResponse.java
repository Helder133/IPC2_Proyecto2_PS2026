package org.proyecto2.proyecto2.dtos.usuario;

import org.proyecto2.proyecto2.models.usuario.Usuario;

public class UsuarioResponse extends UsuarioRequest {
    private int usuarioId;
    private boolean estado;
    // Se ira agregando mas datos a enviar con forme se valla avanzando en el proyecto
    public UsuarioResponse(Usuario usuario) {
        this.usuarioId = usuario.getUsuarioId();
        this.nombreCompleto = usuario.getNombreCompleto();
        this.userName = usuario.getUserName();
        this.password = "";
        this.email = usuario.getEmail();
        this.telefono = usuario.getTelefono();
        this.direccion = usuario.getDireccion();
        this.cui = usuario.getCui();
        this.fechaNacimiento = usuario.getFechaNacimiento();
        this.rol = usuario.getRol();
        this.estado = usuario.isEstado();
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
