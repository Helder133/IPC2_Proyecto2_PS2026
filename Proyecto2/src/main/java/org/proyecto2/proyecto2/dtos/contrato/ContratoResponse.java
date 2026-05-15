package org.proyecto2.proyecto2.dtos.contrato;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.proyecto2.proyecto2.models.contrato.Contrato;
import org.proyecto2.proyecto2.models.contrato.EnumContrato;

import java.time.LocalDate;

public class ContratoResponse {
    private int contratoId;
    private int propuestaId;
    private EnumContrato estado;
    private String motivoCancelacion;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate fechaCreacion;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate fechaFinalizacion;
    private String comentario;
    private int calificacion;
    private String tituloProyecto;

    public ContratoResponse(Contrato contrato) {
        this.contratoId = contrato.getContratoId();
        this.propuestaId = contrato.getPropuestaId();
        this.estado = contrato.getEstado();
        this.motivoCancelacion = contrato.getMotivoCancelacion();
        this.fechaCreacion = contrato.getFechaCreacion();
        this.fechaFinalizacion = contrato.getFechaFinalizacion();
        this.comentario = contrato.getComentario();
        this.calificacion = contrato.getCalificacion();
        this.tituloProyecto = contrato.getTituloProyecto();
    }

    public int getContratoId() {
        return contratoId;
    }

    public void setContratoId(int contratoId) {
        this.contratoId = contratoId;
    }

    public int getPropuestaId() {
        return propuestaId;
    }

    public void setPropuestaId(int propuestaId) {
        this.propuestaId = propuestaId;
    }

    public EnumContrato getEstado() {
        return estado;
    }

    public void setEstado(EnumContrato estado) {
        this.estado = estado;
    }

    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }

    public void setMotivoCancelacion(String motivoCancelacion) {
        this.motivoCancelacion = motivoCancelacion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(LocalDate fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getTituloProyecto() {
        return tituloProyecto;
    }

    public void setTituloProyecto(String tituloProyecto) {
        this.tituloProyecto = tituloProyecto;
    }
}
