package org.proyecto2.proyecto2.models.usuario;

import org.apache.commons.lang3.StringUtils;
import org.proyecto2.proyecto2.dtos.usuario.UsuarioRequest;
import org.proyecto2.proyecto2.dtos.usuario.UsuarioUpdate;
import org.proyecto2.proyecto2.models.usuario.cliente.Cliente;
import org.proyecto2.proyecto2.models.usuario.freelancer.Freelancer;
import org.proyecto2.proyecto2.utils.HashUtil;

import java.time.LocalDate;

public class Usuario {
    private int usuarioId;
    private String nombreCompleto;
    private String userName;
    private String password;
    private String email;
    private String telefono;
    private String direccion;
    private String cui;
    private LocalDate fechaNacimiento;
    private EnumUsuario rol;
    private boolean estado;
    private Cliente cliente;
    private Freelancer freelancer;

    public Usuario(String nombreCompleto, String userName, String password, String email, String telefono, String direccion, String cui, EnumUsuario rol, LocalDate fechaNacimiento) {
        this.nombreCompleto = nombreCompleto;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.cui = cui;
        this.rol = rol;
        this.fechaNacimiento = fechaNacimiento;
        this.estado = true;
    }

    public Usuario(UsuarioRequest usuarioRequest) {
        this.nombreCompleto = usuarioRequest.getNombreCompleto();
        this.userName = usuarioRequest.getUserName();
        this.password = encriptar(usuarioRequest.getPassword());
        this.email = usuarioRequest.getEmail();
        this.telefono = usuarioRequest.getTelefono();
        this.direccion = usuarioRequest.getDireccion();
        this.cui = usuarioRequest.getCui();
        this.rol = usuarioRequest.getRol();
        this.fechaNacimiento = usuarioRequest.getFechaNacimiento();
        this.estado = true;
    }

    public Usuario(UsuarioUpdate usuarioUpdate) {
        this.nombreCompleto = usuarioUpdate.getNombreCompleto();
        this.userName = usuarioUpdate.getUserName();
        this.password = encriptar(usuarioUpdate.getPassword());
        this.email = usuarioUpdate.getEmail();
        this.telefono = usuarioUpdate.getTelefono();
        this.direccion = usuarioUpdate.getDireccion();
        this.cui = usuarioUpdate.getCui();
        this.fechaNacimiento = usuarioUpdate.getFechaNacimiento();
        this.usuarioId = usuarioUpdate.getUsuarioId();
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Freelancer getFreelancer() {
        return freelancer;
    }

    public void setFreelancer(Freelancer freelancer) {
        this.freelancer = freelancer;
    }

    public boolean isValid() {
        return StringUtils.isNotBlank(nombreCompleto) &&
               StringUtils.isNotBlank(userName) &&
               StringUtils.isNotBlank(password) &&
               StringUtils.isNotBlank(email) &&
               StringUtils.isNotBlank(telefono) &&
               StringUtils.isNotBlank(direccion) &&
               StringUtils.isNotBlank(cui) &&
               fechaNacimiento != null &&
               rol != null;
    }

    public boolean isValidUpdate() {
        return StringUtils.isNotBlank(nombreCompleto) &&
                StringUtils.isNotBlank(userName) &&
                StringUtils.isNotBlank(email) &&
                StringUtils.isNotBlank(telefono) &&
                StringUtils.isNotBlank(direccion) &&
                StringUtils.isNotBlank(cui) &&
                fechaNacimiento != null &&
                usuarioId > 0;
    }

    private String encriptar(String password) {
        if (StringUtils.isBlank(password)) return null;
        return HashUtil.sha256(password);
    }
}
