package org.proyecto2.proyecto2.dtos.usuario;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.proyecto2.proyecto2.dtos.usuario.cliente.ClienteResponse;
import org.proyecto2.proyecto2.dtos.usuario.freelancer.FreelancerResponse;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.models.usuario.Usuario;

import java.time.LocalDate;

public class UsuarioResponse {
    private int usuarioId;
    private String nombreCompleto;
    private String userName;
    private String password;
    private String email;
    private String telefono;
    private String direccion;
    private String cui;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate fechaNacimiento;
    private EnumUsuario rol;
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

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public EnumUsuario getRol() {
        return rol;
    }

    public void setRol(EnumUsuario rol) {
        this.rol = rol;
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
