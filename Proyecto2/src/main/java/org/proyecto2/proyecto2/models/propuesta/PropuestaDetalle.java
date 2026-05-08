package org.proyecto2.proyecto2.models.propuesta;

import java.time.LocalDate;

public class PropuestaDetalle {
    private int propuestaId;
    private EnumPropuesta estado;
    private int proyectoId;
    private int usuarioId;
    private double monto;
    private int tiempoEntrega;
    private String descripcion;
    private LocalDate fechaCreacion;

    private String nombreCompleto;
    private String userName;
    private double promedioCalificacion;
    private int totalCalificacion;

    public PropuestaDetalle(int propuestaId, EnumPropuesta estado, int proyectoId, int usuarioId, double monto, int tiempoEntrega, String descripcion, LocalDate fechaCreacion, String nombreCompleto, String userName, double promedioCalificacion, int totalCalificacion) {
        this.propuestaId = propuestaId;
        this.estado = estado;
        this.proyectoId = proyectoId;
        this.usuarioId = usuarioId;
        this.monto = monto;
        this.tiempoEntrega = tiempoEntrega;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.nombreCompleto = nombreCompleto;
        this.userName = userName;
        this.promedioCalificacion = promedioCalificacion;
        this.totalCalificacion = totalCalificacion;
    }

    public int getPropuestaId() {
        return propuestaId;
    }

    public void setPropuestaId(int propuestaId) {
        this.propuestaId = propuestaId;
    }

    public EnumPropuesta getEstado() {
        return estado;
    }

    public void setEstado(EnumPropuesta estado) {
        this.estado = estado;
    }

    public int getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(int proyectoId) {
        this.proyectoId = proyectoId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public int getTiempoEntrega() {
        return tiempoEntrega;
    }

    public void setTiempoEntrega(int tiempoEntrega) {
        this.tiempoEntrega = tiempoEntrega;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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

    public double getPromedioCalificacion() {
        return promedioCalificacion;
    }

    public void setPromedioCalificacion(double promedioCalificacion) {
        this.promedioCalificacion = promedioCalificacion;
    }

    public int getTotalCalificacion() {
        return totalCalificacion;
    }

    public void setTotalCalificacion(int totalCalificacion) {
        this.totalCalificacion = totalCalificacion;
    }
}
