package com.example.amador.tucanchape.model;

import java.io.Serializable;
import java.util.List;

public class Cancha implements Serializable{

    String name;
    Double precio;
    String tipo;
    List<Horario> horarios;

    public Cancha() {
    }

    public Cancha(String name, Double precio, String tipo, List<Horario> horarios) {
        this.name = name;
        this.precio = precio;
        this.tipo = tipo;
        this.horarios = horarios;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Cancha{" +
                "name='" + name + '\'' +
                ", precio=" + precio +
                ", tipo='" + tipo + '\'' +
                ", horarios=" + horarios +
                '}';
    }
}