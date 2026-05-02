package org.proyecto2.proyecto2.dtos.usuario;

import org.proyecto2.proyecto2.dtos.usuario.cliente.ClienteResponse;
import org.proyecto2.proyecto2.dtos.usuario.freelancer.FreelancerResponse;
import org.proyecto2.proyecto2.models.usuario.Usuario;

public class UsuarioResponse extends UsuarioRequest {
    private int usuarioId;
    private boolean estado;
    private ClienteResponse cliente;
    private FreelancerResponse freelancer;

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
        this.cliente = usuario.getCliente() == null ? null : new ClienteResponse(usuario.getCliente());
        this.freelancer = usuario.getFreelancer() == null ? null : new FreelancerResponse(usuario.getFreelancer());
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

    public ClienteResponse getCliente() {
        return cliente;
    }

    public void setCliente(ClienteResponse cliente) {
        this.cliente = cliente;
    }

    public FreelancerResponse getFreelancer() {
        return freelancer;
    }

    public void setFreelancer(FreelancerResponse freelancer) {
        this.freelancer = freelancer;
    }
}
