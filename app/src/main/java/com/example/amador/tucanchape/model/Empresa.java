package com.example.amador.tucanchape.model;

import java.io.Serializable;

public class Empresa implements Serializable{

    private String nombre;
    private String telefono1;
    private String telefono2;
    private String idusuario;
    private String userAdmin;
    private double lat;
    private double lng;

    public Empresa() {
    }

    public Empresa(String nombre, String telefono1, String telefono2, String idusuario, String userAdmin, double lat, double lng) {
        this.nombre = nombre;
        this.telefono1 = telefono1;
        this.telefono2 = telefono2;
        this.idusuario = idusuario;
        this.userAdmin = userAdmin;
        this.lat = lat;
        this.lng = lng;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public String getUserAdmin() {
        return userAdmin;
    }

    public void setUserAdmin(String userAdmin) {
        this.userAdmin = userAdmin;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "nombre='" + nombre + '\'' +
                ", telefono1='" + telefono1 + '\'' +
                ", telefono2='" + telefono2 + '\'' +
                ", idusuario='" + idusuario + '\'' +
                ", userAdmin='" + userAdmin + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}