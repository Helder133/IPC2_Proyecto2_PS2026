package org.proyecto2.proyecto2.dtos.propuesta;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.proyecto2.proyecto2.models.propuesta.EnumPropuesta;
import org.proyecto2.proyecto2.models.propuesta.PropuestaHistorial;

import java.time.LocalDate;

public class PropuestaHistorialResponse {
    private int propuestaId;
    private int proyectoId;
    private String proyectoTitulo; // 🔥 El dato extra que viene del JOIN
    private double monto;
    private int tiempoEntrega;
    private String descripcion;
    private EnumPropuesta estado;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate fechaCreacion;

    public PropuestaHistorialResponse(PropuestaHistorial propuestaHistorial) {
        this.propuestaId = propuestaHistorial.getPropuestaId();
        this.proyectoId = propuestaHistorial.getProyectoId();
        this.proyectoTitulo = propuestaHistorial.getProyectoTitulo(); // 🔥
        this.monto = propuestaHistorial.getMonto();
        this.tiempoEntrega = propuestaHistorial.getTiempoEntrega();
        this.descripcion = propuestaHistorial.getDescripcion();
        this.estado = propuestaHistorial.getEstado();
        this.fechaCreacion = propuestaHistorial.getFechaCreacion();
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

    public String getProyectoTitulo() {
        return proyectoTitulo;
    }

    public void setProyectoTitulo(String proyectoTitulo) {
        this.proyectoTitulo = proyectoTitulo;
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
}
