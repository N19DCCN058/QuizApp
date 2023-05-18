package com.ptithcm.quizapp.model;

public class NumbAccountsByDay {
    private int day;
    private int count;

    public NumbAccountsByDay(int day, int count) {
        this.day = day;
        this.count = count;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
