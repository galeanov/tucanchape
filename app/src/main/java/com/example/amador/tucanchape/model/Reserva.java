package com.example.amador.tucanchape.model;

import java.io.Serializable;

public class Reserva implements Serializable{

    private String fecha;
    private String reservado;

    public Reserva() {
    }

    public Reserva(String fecha, String reservado) {
        this.fecha = fecha;
        this.reservado = reservado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getReservado() {
        return reservado;
    }

    public void setReservado(String reservado) {
        this.reservado = reservado;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "fecha='" + fecha + '\'' +
                ", reservado='" + reservado + '\'' +
                '}';
    }
}
