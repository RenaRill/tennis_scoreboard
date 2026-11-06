package com.tennisscoreboard.model;

public class GameScore extends Score {
    public GameScore() {
        this.score = 0;
    }

    public void reset() {
        this.score = 0;
    }
}
