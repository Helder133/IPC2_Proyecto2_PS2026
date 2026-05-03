package org.proyecto2.proyecto2.dtos.usuario.cartera;

import org.proyecto2.proyecto2.models.usuario.cartera.Cartera;

public class CarteraResponse extends CarteraRequest{
    private double saldoBloqueado;

    public CarteraResponse(Cartera cartera) {
        this.usuarioId = cartera.getUsuarioId();
        this.monto = cartera.getSaldo();
        this.saldoBloqueado = cartera.getSaldoBloqueado();
    }

    public double getSaldoBloqueado() {
        return saldoBloqueado;
    }

    public void setSaldoBloqueado(double saldoBloqueado) {
        this.saldoBloqueado = saldoBloqueado;
    }
}
