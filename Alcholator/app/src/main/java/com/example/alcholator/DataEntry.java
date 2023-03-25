package com.example.alcholator;

public class DataEntry {
    private String uid;
    private String weight;
    private String gender;

    public DataEntry() {

    }

    public DataEntry(String uid, String weight, String gender) {
        this.uid = uid;
        this.weight = weight;
        this.gender = gender;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
