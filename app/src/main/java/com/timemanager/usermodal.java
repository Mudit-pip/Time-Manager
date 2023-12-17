package com.timemanager;

public class usermodal {
    String day, time, task, color;

    public usermodal() {
    }

    public usermodal(String day, String time, String task, String color) {
        this.day = day;
        this.time = time;
        this.task = task;
        this.color = color;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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
