package com.example.alcholator;

public class alcoholObj {

        private String name;
        private double volalc;
        private double vol;

        public alcoholObj(String name, double volalc, double vol) {
            this.name = name;
            this.volalc = volalc;
            this.vol = vol;
        }

        public String getName(){
            return name;
        }


        public void setName(String name) {
            this.name = name;
        }
        public Double getVolalc(){
            return volalc;
        }


        public void setVolalc(double volalc) {
            this.volalc = volalc;
        }
        public Double getVol(){
            return vol;
        }

        public void setVol(double vol) {
            this.vol = volalc;
        }


}
