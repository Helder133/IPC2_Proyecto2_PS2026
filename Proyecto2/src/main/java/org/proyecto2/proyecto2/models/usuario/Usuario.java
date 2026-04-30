package org.proyecto2.proyecto2.models.usuario;

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
}
