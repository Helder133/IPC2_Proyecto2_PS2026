package org.proyecto2.proyecto2.models.usuario.cartera;

import java.time.LocalDate;

public class Transaccion {
    private int transaccionId;
    private int usuarioId;
    private EnumTransaccion tipo;
    private double monto;
    private LocalDate fecha;

    public Transaccion(int usuarioId, EnumTransaccion tipo, double monto) {
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.monto = monto;
        this.fecha = LocalDate.now();
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
