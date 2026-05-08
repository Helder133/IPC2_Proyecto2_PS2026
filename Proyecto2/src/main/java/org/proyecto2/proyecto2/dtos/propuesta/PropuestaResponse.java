package org.proyecto2.proyecto2.dtos.propuesta;

import org.proyecto2.proyecto2.models.propuesta.EnumPropuesta;
import org.proyecto2.proyecto2.models.propuesta.Propuesta;

public class PropuestaResponse extends PropuestaRequest{
    private int propuestaId;
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

    public EnumPropuesta getEstado() {
        return estado;
    }

    public void setEstado(EnumPropuesta estado) {
        this.estado = estado;
    }
}
