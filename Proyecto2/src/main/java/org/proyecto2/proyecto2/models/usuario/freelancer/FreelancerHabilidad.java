package org.proyecto2.proyecto2.models.usuario.freelancer;

import org.proyecto2.proyecto2.dtos.usuario.freelancer.FreelancerHabilidadRequest;

public class FreelancerHabilidad {
    private int usuarioId;
    private int habilidadId;

    public FreelancerHabilidad(FreelancerHabilidadRequest freelancerHabilidadRequest, int usuarioId) {
        this.habilidadId = freelancerHabilidadRequest.getHabilidadId();
        this.usuarioId = usuarioId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getHabilidadId() {
        return habilidadId;
    }

    public void setHabilidadId(int habilidadId) {
        this.habilidadId = habilidadId;
    }

    public boolean isValid() {
        return usuarioId > 0
                && habilidadId > 0;
    }
}
