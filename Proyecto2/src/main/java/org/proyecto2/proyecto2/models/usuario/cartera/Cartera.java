package org.proyecto2.proyecto2.models.usuario.cartera;

import org.proyecto2.proyecto2.dtos.usuario.cartera.CarteraRequest;

public class Cartera {
    private int usuarioId;
    private double saldo;
    private double saldoBloqueado;

    public Cartera(int usuarioId) {
        this.usuarioId = usuarioId;
        this.saldo = 0.0;
        this.saldoBloqueado = 0.0;
    }

    public Cartera(int usuarioId, double saldo, double saldoBloqueado) {
        this.usuarioId = usuarioId;
        this.saldo = saldo;
        this.saldoBloqueado = saldoBloqueado;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public double getSaldoBloqueado() {
        return saldoBloqueado;
    }

    public void setSaldoBloqueado(double saldoBloqueado) {
        this.saldoBloqueado = saldoBloqueado;
    }

    public boolean isValid() {
        return saldo > 0
                && usuarioId > 0;
    }
}
