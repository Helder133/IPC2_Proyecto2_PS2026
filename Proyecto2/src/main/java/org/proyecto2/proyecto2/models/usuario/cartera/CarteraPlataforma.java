package org.proyecto2.proyecto2.models.usuario.cartera;

public class CarteraPlataforma {
    private int plataformaId;
    private double saldo;

    public CarteraPlataforma( double saldo) {
        this.saldo = saldo;
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
