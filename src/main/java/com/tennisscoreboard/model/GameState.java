package com.tennisscoreboard.model;

public interface GameState {
    boolean addPoint(PlayerSide side);

    String getPoints(PlayerSide side);

    boolean isTieBreak();
}
