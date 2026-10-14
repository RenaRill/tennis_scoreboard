package com.tennisscoreboard.model;

import lombok.Getter;

@Getter
public abstract class Score {
    int score;

    public void addScore() {
        score += 1;
    }

}
