package com.example.alcholator;

public class HistoryEntry {
    private String uid;
    private String date;
    private String bloodResult;

    public HistoryEntry() {

    }

    public HistoryEntry(String uid, String date, String bloodResult) {
        this.uid = uid;
        this.date = date;
        this.bloodResult = bloodResult;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBloodResult() {
        return bloodResult;
    }

    public void setBloodResult(String bloodResult) {
        this.bloodResult = bloodResult;
    }
}
