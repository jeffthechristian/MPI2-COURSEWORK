package com.example.alcholator;

public class alcoholObj {

    private String name;
    private double volalc;
    private double vol;

    public alcoholObj() {
        // Required default constructor for Firebase Realtime Database
    }

    public alcoholObj(String name, double volalc, double vol) {
        this.name = name;
        this.volalc = volalc;
        this.vol = vol;
    }

    public String getName() {
        return name;
    }

    public Double getVolalc() {
        return volalc;
    }

    public Double getVol() {
        return vol;
    }
}
