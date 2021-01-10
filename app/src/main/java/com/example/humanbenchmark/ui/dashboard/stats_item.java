package com.example.humanbenchmark.ui.dashboard;

public class stats_item {
    private String name;
    private int pos;
    private String score;
    private String perc;

    public stats_item(String name, int pos, String score, String perc) {
        this.pos = pos;
        this.name = name;
        this.score = score;
        this.perc = perc;
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

    public String getScore() {
        return score;
    }

    public String getPerc() {
        return perc;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setPerc(String perc) {
        this.perc = perc;
    }
}
