package org.proyecto2.proyecto2.dtos.usuario.freelancer;

import org.proyecto2.proyecto2.dtos.habilidad.HabilidadResponse;
import org.proyecto2.proyecto2.models.usuario.freelancer.EnumFreelancer;
import org.proyecto2.proyecto2.models.usuario.freelancer.Freelancer;

import java.util.List;

public class FreelancerResponse {
    private int usuarioId;
    private String descripcion;
    private EnumFreelancer experiencia;
    private double tarifaHora;
    private List<HabilidadResponse> habilidadResponses;

    public FreelancerResponse(Freelancer freelancer) {
        this.usuarioId = freelancer.getUsuarioId();
        this.descripcion = freelancer.getDescripcion();
        this.experiencia = freelancer.getExperiencia();
        this.tarifaHora = freelancer.getTarifaHora();
        this.habilidadResponses = freelancer.getHabilidades() != null ? freelancer.getHabilidades().stream().map(HabilidadResponse::new).toList() : null;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EnumFreelancer getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(EnumFreelancer experiencia) {
        this.experiencia = experiencia;
    }

    public double getTarifaHora() {
        return tarifaHora;
    }

    public void setTarifaHora(double tarifaHora) {
        this.tarifaHora = tarifaHora;
    }

    public List<HabilidadResponse> getHabilidadResponses() {
        return habilidadResponses;
    }

    public void setHabilidadResponses(List<HabilidadResponse> habilidadResponses) {
        this.habilidadResponses = habilidadResponses;
    }
}
