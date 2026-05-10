package org.proyecto2.proyecto2.dtos.entrega;

import org.proyecto2.proyecto2.models.entrega.Entrega;
import org.proyecto2.proyecto2.models.entrega.EnumEntrega;

import java.time.LocalDate;

public class EntregaResponse {
    private int entregaId;
    private int contratoId;
    private String descripcion;
    private String archivo;
    private EnumEntrega estado;
    private String motivo_rechazo;
    private LocalDate fecha;

    public EntregaResponse(Entrega entrega) {
        this.entregaId = entrega.getEntregaId();
        this.contratoId = entrega.getContratoId();
        this.descripcion = entrega.getDescripcion();
        this.archivo = entrega.getArchivo();
        this.estado = entrega.getEstado();
        this.motivo_rechazo = entrega.getMotivo_rechazo();
        this.fecha = entrega.getFecha();
    }

    public int getEntregaId() {
        return entregaId;
    }

    public void setEntregaId(int entregaId) {
        this.entregaId = entregaId;
    }

    public int getContratoId() {
        return contratoId;
    }

    public void setContratoId(int contratoId) {
        this.contratoId = contratoId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public EnumEntrega getEstado() {
        return estado;
    }

    public void setEstado(EnumEntrega estado) {
        this.estado = estado;
    }

    public String getMotivo_rechazo() {
        return motivo_rechazo;
    }

    public void setMotivo_rechazo(String motivo_rechazo) {
        this.motivo_rechazo = motivo_rechazo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
