package com.example.alcholator;

public class DataEntry {
    private String uid;
    private String weight;
    private String gender;

    public DataEntry() {
        // Required default constructor for Firebase Realtime Database
    }

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
