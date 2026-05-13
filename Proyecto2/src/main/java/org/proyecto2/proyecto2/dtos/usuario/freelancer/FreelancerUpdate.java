package org.proyecto2.proyecto2.dtos.usuario.freelancer;

import org.proyecto2.proyecto2.models.usuario.freelancer.EnumFreelancer;

public class FreelancerUpdate {
    private int usuarioId;
    private String descripcion;
    private EnumFreelancer experiencia;
    private double tarifaHora;

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
}
