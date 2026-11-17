package com.tennisscoreboard.model;

public record PlayerScore(String name, String points, int games, int sets, boolean tieBreak) {
}
