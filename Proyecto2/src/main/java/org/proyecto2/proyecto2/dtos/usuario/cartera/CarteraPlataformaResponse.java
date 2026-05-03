package org.proyecto2.proyecto2.dtos.usuario.cartera;

import org.proyecto2.proyecto2.models.usuario.cartera.CarteraPlataforma;

public class CarteraPlataformaResponse {
    private int plataformaId;
    private double saldo;

    public CarteraPlataformaResponse(CarteraPlataforma carteraPlataforma) {
        this.plataformaId = carteraPlataforma.getPlataformaId();
        this.saldo = carteraPlataforma.getSaldo();
    }

    public int getPlataformaId() {
        return plataformaId;
    }

    public void setPlataformaId(int plataformaId) {
        this.plataformaId = plataformaId;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
