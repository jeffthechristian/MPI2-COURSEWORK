package com.example.alcholator;

public class DataEntry {
    private final String uid;
    private final String weight;
    private final String gender;

    public DataEntry(String uid, String weight, String gender) {
        this.uid = uid;
        this.weight = weight;
        this.gender = gender;
    }

    public String getWeight() {
        return weight;
    }
    public String getGender() {
        return gender;
    }
}
