package org.proyecto2.proyecto2.models.usuario.freelancer;

import org.apache.commons.lang3.StringUtils;
import org.proyecto2.proyecto2.dtos.usuario.freelancer.FreelancerRequest;
import org.proyecto2.proyecto2.dtos.usuario.freelancer.FreelancerUpdate;
import org.proyecto2.proyecto2.models.habilidad.Habilidad;

import java.util.List;

public class Freelancer {
    private int usuarioId;
    private String descripcion;
    private EnumFreelancer experiencia;
    private double tarifaHora;
    private List<Habilidad> habilidades;

    public Freelancer(int usuarioId, String descripcion, EnumFreelancer experiencia, double tarifaHora) {
        this.usuarioId = usuarioId;
        this.descripcion = descripcion;
        this.experiencia = experiencia;
        this.tarifaHora = tarifaHora;
    }

    public Freelancer(FreelancerRequest freelancerRequest) {
        this.usuarioId = freelancerRequest.getUsuarioId();
        this.descripcion = freelancerRequest.getDescripcion();
        this.experiencia = freelancerRequest.getExperiencia();
        this.tarifaHora = freelancerRequest.getTarifaHora();
    }

    public Freelancer(FreelancerUpdate freelancerUpdate) {
        this.usuarioId = freelancerUpdate.getUsuarioId();
        this.descripcion = freelancerUpdate.getDescripcion();
        this.experiencia = freelancerUpdate.getExperiencia();
        this.tarifaHora = freelancerUpdate.getTarifaHora();
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

    public List<Habilidad> getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(List<Habilidad> habilidades) {
        this.habilidades = habilidades;
    }

    public boolean isValid() {
        return usuarioId > 0
                && StringUtils.isNotBlank(descripcion)
                && experiencia != null
                && tarifaHora > 0
                && !habilidades.isEmpty();
    }
}
