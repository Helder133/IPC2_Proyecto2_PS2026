package org.proyecto2.proyecto2.models.entrega;

import org.apache.commons.lang3.StringUtils;
import org.proyecto2.proyecto2.dtos.entrega.EntregaRechazada;
import org.proyecto2.proyecto2.dtos.entrega.EntregaRequest;
import org.proyecto2.proyecto2.dtos.entrega.EntregaUpdate;

import java.time.LocalDate;

public class Entrega {
    private int entregaId;
    private int contratoId;
    private String descripcion;
    private String archivo;
    private EnumEntrega estado;
    private String motivo_rechazo;
    private LocalDate fecha;

    public Entrega(int entregaId, int contratoId, String descripcion, String archivo, EnumEntrega estado, String motivo_rechazo, LocalDate fecha) {
        this.entregaId = entregaId;
        this.contratoId = contratoId;
        this.descripcion = descripcion;
        this.archivo = archivo;
        this.estado = estado;
        this.motivo_rechazo = motivo_rechazo;
        this.fecha = fecha;
    }

    public Entrega(EntregaRequest entregaRequest) {
        this.contratoId = entregaRequest.getContratoId();
        this.descripcion = entregaRequest.getDescripcion();
        this.archivo = entregaRequest.getArchivo();
        this.estado = EnumEntrega.PENDIENTE;
        this.fecha = LocalDate.now();
    }

    public boolean isValid() {
        return contratoId > 0
                && StringUtils.isNotBlank(descripcion)
                && StringUtils.isNotBlank(archivo)
                && estado != null
                && fecha != null;
    }

    public Entrega(EntregaUpdate entregaUpdate) {
        this.entregaId = entregaUpdate.getEntregaId();
        this.descripcion = entregaUpdate.getDescripcion();
        this.archivo = entregaUpdate.getArchivo();
    }

    public boolean isValidUpdate() {
        return entregaId > 0
                && StringUtils.isNotBlank(descripcion)
                && StringUtils.isNotBlank(archivo);
    }

    public Entrega(EntregaRechazada entregaRechazada) {
        this.entregaId = entregaRechazada.getEntregaId();
        this.motivo_rechazo = entregaRechazada.getMotivo_rechazo();
        this.estado = EnumEntrega.RECHAZADA;
    }

    public boolean isValidRechazo() {
        return entregaId > 0
                && StringUtils.isNotBlank(motivo_rechazo)
                && estado != null;
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
