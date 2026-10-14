package com.tennisscoreboard.model;

import com.tennisscoreboard.exception.InvalidValueException;

public class Set extends Score {
    public Set() {
        this.score = 0;
    }

    public void addScore() {
        if (score < 2) {
            score += 1;
        } else {
            throw new InvalidValueException("Игрок уже выиграл максимально допустимое количество сетов.");
        }
    }
}
