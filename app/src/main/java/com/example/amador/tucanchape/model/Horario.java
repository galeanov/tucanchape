package com.example.amador.tucanchape.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Horario implements Serializable{

    private String desde;
    private String hasta;
    private List<Reserva> reservas;

    public Horario() {
    }

    public Horario(String desde, String hasta, List<Reserva> reservas) {
        this.desde = desde;
        this.hasta = hasta;
        this.reservas = reservas;
    }

    public String getDesde() {
        return desde;
    }

    public void setDesde(String desde) {
        this.desde = desde;
    }

    public String getHasta() {
        return hasta;
    }

    public void setHasta(String hasta) {
        this.hasta = hasta;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    @Override
    public String toString() {
        return "Horario{" +
                "desde='" + desde + '\'' +
                ", hasta='" + hasta + '\'' +
                ", reservas=" + reservas +
                '}';
    }
}
