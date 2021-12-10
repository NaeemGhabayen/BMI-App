package com.nmg.bmi_app.model;

public class Record {
    String date;
    String time;
    String lenght ;
    String wieght;
    String userId;
    public Record() {
    }

    public Record(String date, String time, String lenght, String wieght, String userId) {
        this.date = date;
        this.time = time;
        this.lenght = lenght;
        this.wieght = wieght;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLenght() {
        return lenght;
    }

    public void setLenght(String lenght) {
        this.lenght = lenght;
    }

    public String getWieght() {
        return wieght;
    }

    public void setWieght(String wieght) {
        this.wieght = wieght;
    }
}
