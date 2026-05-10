package org.proyecto2.proyecto2.models.configuracionSistema;

import org.proyecto2.proyecto2.dtos.configuracionSistema.ConfiguracionSistemaRequest;

import java.time.LocalDate;

public class ConfiguracionSistema {
    private int configuracionId;
    private double comision;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public ConfiguracionSistema(int configuracionId, double comision, LocalDate fechaInicio, LocalDate fechaFin) {
        this.configuracionId = configuracionId;
        this.comision = comision;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public ConfiguracionSistema(ConfiguracionSistemaRequest configuracionSistemaRequest) {
        this.comision = configuracionSistemaRequest.getComision();
        this.fechaInicio = configuracionSistemaRequest.getFechaInicio();
    }

    public boolean isValid() {
        return comision > 0
                && fechaInicio != null;
    }

    public int getConfiguracionId() {
        return configuracionId;
    }

    public void setConfiguracionId(int configuracionId) {
        this.configuracionId = configuracionId;
    }

    public double getComision() {
        return comision;
    }

    public void setComision(double comision) {
        this.comision = comision;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
}
