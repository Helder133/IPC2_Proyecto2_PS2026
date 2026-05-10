package org.proyecto2.proyecto2.dtos.nueva_h_c;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.proyecto2.proyecto2.models.nueva_h_c.EnumNueva_h_cEstado;
import org.proyecto2.proyecto2.models.nueva_h_c.EnumNueva_h_cTipo;
import org.proyecto2.proyecto2.models.nueva_h_c.Nueva_h_c;

import java.time.LocalDate;

public class Nueva_h_cResponse {
    private int solicitudId;
    private int usuarioId;
    private String nombre;
    private String descripcion;
    private EnumNueva_h_cTipo tipo;
    private EnumNueva_h_cEstado estado;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate fechaCreacion;

    public Nueva_h_cResponse(Nueva_h_c nueva_h_c) {
        this.solicitudId = nueva_h_c.getSolicitudId();
        this.usuarioId = nueva_h_c.getUsuarioId();
        this.nombre = nueva_h_c.getNombre();
        this.descripcion = nueva_h_c.getDescripcion();
        this.tipo = nueva_h_c.getTipo();
        this.estado = nueva_h_c.getEstado();
        this.fechaCreacion = nueva_h_c.getFechaCreacion();
    }

    public int getSolicitudId() {
        return solicitudId;
    }

    public void setSolicitudId(int solicitudId) {
        this.solicitudId = solicitudId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EnumNueva_h_cTipo getTipo() {
        return tipo;
    }

    public void setTipo(EnumNueva_h_cTipo tipo) {
        this.tipo = tipo;
    }

    public EnumNueva_h_cEstado getEstado() {
        return estado;
    }

    public void setEstado(EnumNueva_h_cEstado estado) {
        this.estado = estado;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
