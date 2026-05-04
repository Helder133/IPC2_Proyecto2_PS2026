package org.proyecto2.proyecto2.dtos.usuario.freelancer;

import org.proyecto2.proyecto2.dtos.habilidad.HabilidadResponse;
import org.proyecto2.proyecto2.models.usuario.freelancer.Freelancer;

import java.util.List;

public class FreelancerResponse extends FreelancerRequest {
    private List<HabilidadResponse> habilidadResponses;

    public FreelancerResponse(Freelancer freelancer) {
        this.usuarioId = freelancer.getUsuarioId();
        this.descripcion = freelancer.getDescripcion();
        this.experiencia = freelancer.getExperiencia();
        this.tarifaHora = freelancer.getTarifaHora();
        this.habilidadResponses = freelancer.getHabilidades() != null ? freelancer.getHabilidades().stream().map(HabilidadResponse::new).toList() : null;
    }

    public List<HabilidadResponse> getHabilidadesResponse() {
        return habilidadResponses;
    }

    public void setHabilidadResponses(List<HabilidadResponse> habilidadResponses) {
        this.habilidadResponses = habilidadResponses;
    }
}
