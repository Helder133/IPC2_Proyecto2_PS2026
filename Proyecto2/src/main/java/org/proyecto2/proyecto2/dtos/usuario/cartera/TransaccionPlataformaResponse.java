package org.proyecto2.proyecto2.dtos.usuario.cartera;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.proyecto2.proyecto2.models.usuario.cartera.TransaccionPlataforma;

import java.time.LocalDate;

public class TransaccionPlataformaResponse {
    private int transaccionId;
    private int plataformaId;
    private int contratoId;
    private double porcentajeAplicado;
    private double monto_comision;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate fecha;

    public TransaccionPlataformaResponse(TransaccionPlataforma transaccionPlataforma) {
        this.transaccionId = transaccionPlataforma.getTransaccionId();
        this.plataformaId = transaccionPlataforma.getPlataformaId();
        this.contratoId = transaccionPlataforma.getContratoId();
        this.porcentajeAplicado = transaccionPlataforma.getPorcentajeAplicado();
        this.monto_comision = transaccionPlataforma.getMonto_comision();
        this.fecha = transaccionPlataforma.getFecha();
    }

    public int getTransaccionId() {
        return transaccionId;
    }

    public void setTransaccionId(int transaccionId) {
        this.transaccionId = transaccionId;
    }

    public int getPlataformaId() {
        return plataformaId;
    }

    public void setPlataformaId(int plataformaId) {
        this.plataformaId = plataformaId;
    }

    public int getContratoId() {
        return contratoId;
    }

    public void setContratoId(int contratoId) {
        this.contratoId = contratoId;
    }

    public double getPorcentajeAplicado() {
        return porcentajeAplicado;
    }

    public void setPorcentajeAplicado(double porcentajeAplicado) {
        this.porcentajeAplicado = porcentajeAplicado;
    }

    public double getMonto_comision() {
        return monto_comision;
    }

    public void setMonto_comision(double monto_comision) {
        this.monto_comision = monto_comision;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
