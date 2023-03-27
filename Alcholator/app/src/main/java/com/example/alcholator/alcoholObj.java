package com.example.alcholator;

public class alcoholObj {

    private final String name;
    private final double volalc;
    private final double vol;

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
