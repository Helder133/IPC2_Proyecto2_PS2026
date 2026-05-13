package org.proyecto2.proyecto2.dtos.usuario.cartera;

import org.proyecto2.proyecto2.models.usuario.cartera.Cartera;

public class CarteraResponse extends CarteraRequest{
    private int usuarioId;
    private double monto;
    private double saldoBloqueado;

    public CarteraResponse(Cartera cartera) {
        this.usuarioId = cartera.getUsuarioId();
        this.monto = cartera.getSaldo();
        this.saldoBloqueado = cartera.getSaldoBloqueado();
    }

    @Override
    public int getUsuarioId() {
        return usuarioId;
    }

    @Override
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public double getMonto() {
        return monto;
    }

    @Override
    public void setMonto(double monto) {
        this.monto = monto;
    }

    public double getSaldoBloqueado() {
        return saldoBloqueado;
    }

    public void setSaldoBloqueado(double saldoBloqueado) {
        this.saldoBloqueado = saldoBloqueado;
    }
}
