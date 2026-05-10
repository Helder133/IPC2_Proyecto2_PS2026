package org.proyecto2.proyecto2.dtos.configuracionSistema;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.proyecto2.proyecto2.models.configuracionSistema.ConfiguracionSistema;

import java.time.LocalDate;

public class ConfiguracionSistemaResponse {
    private int configuracionId;
    private double comision;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate fechaInicio;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate fechaFin;

    public ConfiguracionSistemaResponse(ConfiguracionSistema configuracionSistema) {
        this.configuracionId = configuracionSistema.getConfiguracionId();
        this.comision = configuracionSistema.getComision();
        this.fechaInicio = configuracionSistema.getFechaInicio();
        this.fechaFin = configuracionSistema.getFechaFin();
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
