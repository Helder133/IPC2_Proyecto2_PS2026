package org.proyecto2.proyecto2.dtos.propuesta;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.proyecto2.proyecto2.models.propuesta.EnumPropuesta;
import org.proyecto2.proyecto2.models.propuesta.PropuestaDetalle;

import java.time.LocalDate;

public class PropuestaDetalleResponse {
    private int propuestaId;
    private EnumPropuesta estado;
    private int proyectoId;
    private int usuarioId;
    private double monto;
    private int tiempoEntrega;
    private String descripcion;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate fechaCreacion;

    private String nombreCompleto;
    private String userName;
    private double promedioCalificacion;
    private int totalCalificacion;

    public PropuestaDetalleResponse(PropuestaDetalle propuestaDetalle) {
        this.propuestaId = propuestaDetalle.getPropuestaId();
        this.estado = propuestaDetalle.getEstado();
        this.proyectoId = propuestaDetalle.getProyectoId();
        this.usuarioId = propuestaDetalle.getUsuarioId();
        this.monto = propuestaDetalle.getMonto();
        this.tiempoEntrega = propuestaDetalle.getTiempoEntrega();
        this.descripcion = propuestaDetalle.getDescripcion();
        this.fechaCreacion = propuestaDetalle.getFechaCreacion();
        this.nombreCompleto = propuestaDetalle.getNombreCompleto();
        this.userName = propuestaDetalle.getUserName();
        this.promedioCalificacion = propuestaDetalle.getPromedioCalificacion();
        this.totalCalificacion = propuestaDetalle.getTotalCalificacion();
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
