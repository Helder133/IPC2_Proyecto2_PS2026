package org.proyecto2.proyecto2.models.usuario.cartera;

import java.time.LocalDate;

public class TransaccionPlataforma {
    private int transaccionId;
    private int plataformaId;
    private int contratoId;
    private double porcentajeAplicado;
    private double monto_comision;
    private LocalDate fecha;

    public TransaccionPlataforma(int transaccionId, int plataformaId, int contratoId, double porcentajeAplicado, double monto_comision, LocalDate fecha) {
        this.transaccionId = transaccionId;
        this.plataformaId = plataformaId;
        this.contratoId = contratoId;
        this.porcentajeAplicado = porcentajeAplicado;
        this.monto_comision = monto_comision;
        this.fecha = fecha;
    }

    public TransaccionPlataforma(int plataformaId, int contratoId, double porcentajeAplicado, double monto_comision) {
        this.plataformaId = plataformaId;
        this.contratoId = contratoId;
        this.porcentajeAplicado = porcentajeAplicado;
        this.monto_comision = monto_comision;
        this.fecha = LocalDate.now();
    }

    public boolean isValid() {
        return plataformaId > 0
                && contratoId > 0
                && porcentajeAplicado > 0
                && monto_comision > 0
                && fecha != null;
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
