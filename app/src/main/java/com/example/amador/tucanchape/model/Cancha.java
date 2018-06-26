package com.example.amador.tucanchape.model;

import java.io.Serializable;
import java.util.List;

public class Cancha implements Serializable{

    String name;
    Double precioD;
    Double precioN;
    String tipo;
    List<Horario> horarios;

    public Cancha() {
    }

    public Cancha(String name, Double precioD, Double precioN, String tipo, List<Horario> horarios) {
        this.name = name;
        this.precioD = precioD;
        this.precioN = precioN;
        this.tipo = tipo;
        this.horarios = horarios;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrecioD() {
        return precioD;
    }

    public void setPrecioD(Double precioD) {
        this.precioD = precioD;
    }

    public Double getPrecioN() {
        return precioN;
    }

    public void setPrecioN(Double precioN) {
        this.precioN = precioN;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }

    @Override
    public String toString() {
        return "Cancha{" +
                "name='" + name + '\'' +
                ", precioD=" + precioD +
                ", precioN=" + precioN +
                ", tipo='" + tipo + '\'' +
                ", horarios=" + horarios +
                '}';
    }
}