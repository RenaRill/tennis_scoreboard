package com.tennisscoreboard.model;

import com.tennisscoreboard.exception.InvalidValueException;
import lombok.Getter;

@Getter
public class PlayerScore {
    private String name;
    private Points points;
    private Game games;
    private Set sets;
    private TieBreak tieBreakPoints;

    public PlayerScore(String name) {
        this.name = name;
        this.points = Points.LOVE;
        this.games = new Game();
        this.sets = new Set();
        this.tieBreakPoints = null;
    }

    public void addPoint(Points scorerPoints) {
        switch (scorerPoints) {
            case LOVE -> this.points = Points.FIFTEEN;
            case FIFTEEN -> this.points = Points.THIRTY;
            case THIRTY -> this.points = Points.FORTY;
            case FORTY -> this.points = Points.ADVANTAGE;
            default -> throw new InvalidValueException("Попытка обновления неправильного состояния.");
        }
    }

    public void addGame(Game scorerGame) {
        scorerGame.addScore();
    }

    public void addSet(Set scorerSet) {
        scorerSet.addScore();
    }

    public void addTieBreakPoint(TieBreak scorerPoints) {
        scorerPoints.addScore();
    }

    public void nullPoints() {
        this.points = null;
    }

    public void startTieBreak() {
        tieBreakPoints.start();
    }
}
