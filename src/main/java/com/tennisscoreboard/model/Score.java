package com.tennisscoreboard.model;

import lombok.Getter;

@Getter
public abstract class Score {
    protected int score;

    public int add() {
        score++;
        return score;
    }
}
