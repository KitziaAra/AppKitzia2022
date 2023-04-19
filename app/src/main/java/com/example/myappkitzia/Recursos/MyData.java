package com.example.myappkitzia.Recursos;

import android.location.Location;

public class MyData {
    private String nombre, pswd;
    private Location location;
    private String imageP;
    private int image;
    private boolean tipo;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getImageP() {
        return imageP;
    }

    public void setImageP(String imageP) {
        this.imageP = imageP;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isTipo() {
        return tipo;
    }

    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }
}
