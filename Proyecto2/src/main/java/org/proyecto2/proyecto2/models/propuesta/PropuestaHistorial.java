package org.proyecto2.proyecto2.models.propuesta;

import java.time.LocalDate;

public class PropuestaHistorial {
    private int propuestaId;
    private int proyectoId;
    private String proyectoTitulo;
    private double monto;
    private int tiempoEntrega;
    private String descripcion;
    private EnumPropuesta estado;
    private LocalDate fechaCreacion;

    public PropuestaHistorial(int propuestaId, int proyectoId, String proyectoTitulo, double monto, int tiempoEntrega, String descripcion, EnumPropuesta estado, LocalDate fechaCreacion) {
        this.propuestaId = propuestaId;
        this.proyectoId = proyectoId;
        this.proyectoTitulo = proyectoTitulo;
        this.monto = monto;
        this.tiempoEntrega = tiempoEntrega;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
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
