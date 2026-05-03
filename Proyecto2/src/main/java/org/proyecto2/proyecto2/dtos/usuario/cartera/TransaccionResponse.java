package org.proyecto2.proyecto2.dtos.usuario.cartera;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.proyecto2.proyecto2.models.usuario.cartera.EnumTransaccion;
import org.proyecto2.proyecto2.models.usuario.cartera.Transaccion;

import java.time.LocalDate;

public class TransaccionResponse {
    private int transaccionId;
    private int usuarioId;
    private EnumTransaccion tipo;
    private double monto;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate fecha;

    public TransaccionResponse(Transaccion transaccion) {
        this.transaccionId = transaccion.getTransaccionId();
        this.usuarioId = transaccion.getUsuarioId();
        this.tipo = transaccion.getTipo();
        this.monto = transaccion.getMonto();
        this.fecha = transaccion.getFecha();
    }

    public int getTransaccionId() {
        return transaccionId;
    }

    public void setTransaccionId(int transaccionId) {
        this.transaccionId = transaccionId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public EnumTransaccion getTipo() {
        return tipo;
    }

    public void setTipo(EnumTransaccion tipo) {
        this.tipo = tipo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
