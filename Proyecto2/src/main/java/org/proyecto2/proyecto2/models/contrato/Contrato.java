package org.proyecto2.proyecto2.models.contrato;

import java.time.LocalDate;

public class Contrato {
    private int contratoId;
    private int propuestaId;
    private EnumContrato estado;
    private String motivoCancelacion;
    private LocalDate fechaCreacion;
    private LocalDate fechaFinalizacion;
    private String comentario;
    private int calificacion;

    public Contrato(int contratoId, int propuestaId, EnumContrato estado, String motivoCancelacion, LocalDate fechaCreacion, LocalDate fechaFinalizacion, String comentario, int calificacion) {
        this.contratoId = contratoId;
        this.propuestaId = propuestaId;
        this.estado = estado;
        this.motivoCancelacion = motivoCancelacion;
        this.fechaCreacion = fechaCreacion;
        this.fechaFinalizacion = fechaFinalizacion;
        this.comentario = comentario;
        this.calificacion = calificacion;
    }

    public Contrato(int propuestaId) {
        this.propuestaId = propuestaId;
        this.estado = EnumContrato.ACTIVO;
        this.fechaCreacion = LocalDate.now();
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

    public boolean isValid() {
        return propuestaId > 0
                && estado != null
                && fechaCreacion != null;
    }
}
