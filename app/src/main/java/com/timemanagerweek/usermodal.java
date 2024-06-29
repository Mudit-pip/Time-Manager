package com.timemanagerweek;

public class usermodal {
    String day, time, task, color, requestcode;

    public usermodal() {
    }

    public usermodal(String day, String time, String task, String color, String requestcode) {
        this.day = day;
        this.time = time;
        this.task = task;
        this.color = color;
        this.requestcode = requestcode;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getRequestcode() {
        return requestcode;
    }

    public void setRequestcode(String requestcode) {
        this.requestcode = requestcode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
