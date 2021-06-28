package com.ejemplo.testpedidosya.Pojo;

import java.io.Serializable;

public class Restaurant implements Serializable {

    private String name;
    private double latitude;
    private double longitud;

    public Restaurant(String name, double latitude, double longitud) {
        this.name = name;
        this.latitude = latitude;
        this.longitud = longitud;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

}
