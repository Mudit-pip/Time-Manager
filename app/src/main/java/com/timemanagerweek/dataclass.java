package com.timemanagerweek;

public class dataclass {
    String time, day;

    public dataclass() {
    }

    public dataclass(String time, String day) {
        this.time = time;
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
