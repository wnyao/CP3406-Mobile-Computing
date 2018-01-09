package com.example.kongwenyao.educationalgame;

/**
 * Created by kongwenyao on 1/9/18.
 */

public class Score {

    private int id;
    private int score;
    private String playerName;

    public Score() {
        //Empty constructor
    }

    public Score(int id, String playerName, int score) {
        this.id = id;
        this.score = score;
        this.playerName = playerName;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
