package org.proyecto2.proyecto2.dtos.usuario.freelancer;

import org.proyecto2.proyecto2.models.usuario.freelancer.Freelancer;

public class FreelancerResponse extends FreelancerRequest {
    public FreelancerResponse(Freelancer freelancer) {
        this.usuarioId = freelancer.getUsuarioId();
        this.descripcion = freelancer.getDescripcion();
        this.experiencia = freelancer.getExperiencia();
        this.tarifaHora = freelancer.getTarifaHora();
    }
}
