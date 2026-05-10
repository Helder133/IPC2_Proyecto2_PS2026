package org.proyecto2.proyecto2.models.nueva_h_c;

import org.apache.commons.lang3.StringUtils;
import org.proyecto2.proyecto2.dtos.nueva_h_c.Nueva_h_cRequest;
import org.proyecto2.proyecto2.dtos.nueva_h_c.Nueva_h_cUpdate;

import java.time.LocalDate;

public class Nueva_h_c {
    private int solicitudId;
    private int usuarioId;
    private String nombre;
    private String descripcion;
    private EnumNueva_h_cTipo tipo;
    private EnumNueva_h_cEstado estado;
    private LocalDate fechaCreacion;

    public Nueva_h_c(int solicitudId, int usuarioId, String nombre, String descripcion, EnumNueva_h_cTipo tipo, EnumNueva_h_cEstado estado, LocalDate fechaCreacion) {
        this.solicitudId = solicitudId;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public Nueva_h_c(Nueva_h_cRequest nueva_h_cRequest) {
        this.usuarioId = nueva_h_cRequest.getUsuarioId();
        this.nombre = nueva_h_cRequest.getNombre();
        this.descripcion = nueva_h_cRequest.getDescripcion();
        this.tipo = nueva_h_cRequest.getTipo();
        this.estado = EnumNueva_h_cEstado.PENDIENTE;
        this.fechaCreacion = LocalDate.now();
    }

    public boolean isValid() {
        return usuarioId > 0
                && StringUtils.isNotBlank(nombre)
                && StringUtils.isNotBlank(descripcion)
                && tipo != null
                && estado != null
                && fechaCreacion != null;
    }

    public Nueva_h_c(Nueva_h_cUpdate nueva_h_cUpdate) {
        this.solicitudId = nueva_h_cUpdate.getSolicitudId();
        this.nombre = nueva_h_cUpdate.getNombre();
        this.descripcion = nueva_h_cUpdate.getDescripcion();
    }

    public boolean isValidUpdate() {
        return solicitudId > 0
                && StringUtils.isNotBlank(nombre)
                && StringUtils.isNotBlank(descripcion);
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
