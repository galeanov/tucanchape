package com.example.amador.tucanchape.model;

import java.io.Serializable;

public class Reserva implements Serializable{

    private String fecha;
    private String reservado;
    private String siglas;

    public Reserva() {
    }

    public Reserva(String fecha, String reservado, String siglas) {
        this.fecha = fecha;
        this.reservado = reservado;
        this.siglas = siglas;
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

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "fecha='" + fecha + '\'' +
                ", reservado='" + reservado + '\'' +
                ", siglas='" + siglas + '\'' +
                '}';
    }
}
