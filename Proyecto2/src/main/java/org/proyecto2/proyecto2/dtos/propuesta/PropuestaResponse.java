package org.proyecto2.proyecto2.dtos.propuesta;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.proyecto2.proyecto2.models.propuesta.EnumPropuesta;
import org.proyecto2.proyecto2.models.propuesta.Propuesta;

import java.time.LocalDate;

public class PropuestaResponse{
    private int propuestaId;
    private int proyectoId;
    private int usuarioId;
    private double monto;
    private int tiempoEntrega;
    private String descripcion;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate fechaCreacion;
    private EnumPropuesta estado;

    public PropuestaResponse(Propuesta propuesta) {
        this.propuestaId = propuesta.getPropuestaId();
        this.proyectoId = propuesta.getProyectoId();
        this.usuarioId = propuesta.getUsuarioId();
        this.monto = propuesta.getMonto();
        this.tiempoEntrega = propuesta.getTiempoEntrega();
        this.descripcion = propuesta.getDescripcion();
        this.fechaCreacion = propuesta.getFechaCreacion();
        this.estado = propuesta.getEstado();
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

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public EnumPropuesta getEstado() {
        return estado;
    }

    public void setEstado(EnumPropuesta estado) {
        this.estado = estado;
    }
}
