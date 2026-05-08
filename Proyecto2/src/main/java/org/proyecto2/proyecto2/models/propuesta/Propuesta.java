package org.proyecto2.proyecto2.models.propuesta;

import org.apache.commons.lang3.StringUtils;
import org.proyecto2.proyecto2.dtos.propuesta.PropuestaRequest;
import org.proyecto2.proyecto2.dtos.propuesta.PropuestaUpdate;

import java.time.LocalDate;

public class Propuesta {
    private int propuestaId;
    private int proyectoId;
    private int usuarioId;
    private double monto;
    private int tiempoEntrega;
    private String descripcion;
    private EnumPropuesta estado;
    private LocalDate fechaCreacion;

    public Propuesta(int propuestaId, int proyectoId, int usuarioId, double monto, int tiempoEntrega, String descripcion, EnumPropuesta estado, LocalDate fechaCreacion) {
        this.propuestaId = propuestaId;
        this.proyectoId = proyectoId;
        this.usuarioId = usuarioId;
        this.monto = monto;
        this.tiempoEntrega = tiempoEntrega;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public Propuesta(PropuestaRequest propuestaRequest) {
        this.proyectoId = propuestaRequest.getProyectoId();
        this.usuarioId = propuestaRequest.getUsuarioId();
        this.monto = propuestaRequest.getMonto();
        this.tiempoEntrega = propuestaRequest.getTiempoEntrega();
        this.descripcion = propuestaRequest.getDescripcion();
        this.fechaCreacion = propuestaRequest.getFechaCreacion() == null ? LocalDate.now() : propuestaRequest.getFechaCreacion();
        this.estado = EnumPropuesta.PENDIENTE;
    }

    public Propuesta(PropuestaUpdate propuestaUpdate) {
        this.propuestaId = propuestaUpdate.getPropuestaId();
        this.monto = propuestaUpdate.getMonto();
        this.tiempoEntrega = propuestaUpdate.getTiempoEntrega();
        this.descripcion = propuestaUpdate.getDescripcion();
    }

    public int getPropuestaId() {
        return propuestaId;
    }

    public void setPropuestaId(int propuestaId) {
        this.propuestaId = propuestaId;
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

    public EnumPropuesta getEstado() {
        return estado;
    }

    public void setEstado(EnumPropuesta estado) {
        this.estado = estado;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isValid() {
        return proyectoId > 0
                && usuarioId > 0
                && monto > 0
                && tiempoEntrega > 0
                && StringUtils.isNotBlank(descripcion)
                && fechaCreacion != null;
    }

    public boolean isValidUpdate() {
        return propuestaId > 0
                && monto > 0
                && tiempoEntrega > 0
                && StringUtils.isNotBlank(descripcion);
    }
}
