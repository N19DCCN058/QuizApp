package com.ptithcm.quizapp.model;

public class Level {
    int id;
    String title;
    int type;

    public Level() {
    }

    public Level(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public Level(int id, String title, int type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
