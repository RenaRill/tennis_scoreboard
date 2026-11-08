package com.tennisscoreboard.model;

import lombok.Getter;

public class PlayerScore {
    @Getter
    private final String name;
    @Getter
    private Points points;
    private final GameScore games;
    private final SetScore sets;
    private TieBreakScore tieBreakPoints;

    public PlayerScore(String name) {
        this.name = name;
        this.points = Points.LOVE;
        this.games = new GameScore();
        this.sets = new SetScore();
    }

    public void addPoint(Points playerPoints) {
        switch (playerPoints) {
            case LOVE -> this.points = Points.FIFTEEN;
            case FIFTEEN -> this.points = Points.THIRTY;
            case THIRTY -> this.points = Points.FORTY;
        }
    }

    public String getPointsScore() {
        return points.getScore();
    }

    public int getGameScore() {
        return games.getScore();
    }

    public int getSetScore() {
        return sets.getScore();
    }

    public int getTieBreakScore() {
        return tieBreakPoints.getScore();
    }

    public void resetPoints() {
        this.points = Points.LOVE;
    }

    public void removeAdvantage() {
        this.points = Points.FORTY;
    }

    public void setAdvantage() {
        this.points = Points.ADVANTAGE;
    }

    public void addGame() {
        games.add();
    }

    public void resetGames() {
        games.reset();
    }

    public void addSet() {
        sets.add();
    }

    public void addTieBreakPoint() {
        tieBreakPoints.add();
    }

    public void startTieBreak() {
        this.points = null;
        this.tieBreakPoints = new TieBreakScore();
    }

    public void endTieBreak() {
        this.points = Points.LOVE;
        this.tieBreakPoints = null;
    }

    public boolean isTieBreak() {
        return tieBreakPoints != null;
    }
}
