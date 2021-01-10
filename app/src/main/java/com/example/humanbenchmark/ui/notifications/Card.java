package com.example.humanbenchmark.ui.notifications;

public class Card {
    private String name;
    private int pos;
    private int image;

    public Card(String name, int image, int pos) {
        this.pos = pos;
        this.name = name;
        this.image = image;
    }

    public int getPos() {
        return pos;
    }

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
