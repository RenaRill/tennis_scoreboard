package com.tennisscoreboard.model;

public enum PlayerSide {
    FIRST,
    SECOND;

    public PlayerSide getOpponent() {
        return this == FIRST ? SECOND : FIRST;
    }
}
