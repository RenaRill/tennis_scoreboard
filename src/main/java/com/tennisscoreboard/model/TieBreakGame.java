package com.tennisscoreboard.model;

import java.util.EnumMap;
import java.util.Map;

public class TieBreakGame implements GameState {
    private static final int MIN_TIE_BREAK_POINTS_FOR_WIN = 7;
    private static final int MIN_MARGIN = 2;

    private final Map<PlayerSide, Integer> points = new EnumMap<>(PlayerSide.class);

    public TieBreakGame() {
        points.put(PlayerSide.FIRST, 0);
        points.put(PlayerSide.SECOND, 0);
    }

    @Override
    public boolean addPoint(PlayerSide side) {
        int newValue = points.merge(side, 1, Integer::sum);
        int opponentPoints = points.get(side.getOpponent());

        return newValue >= MIN_TIE_BREAK_POINTS_FOR_WIN && newValue - opponentPoints >= MIN_MARGIN;
    }

    @Override
    public String getPoints(PlayerSide side) {
        return String.valueOf(points.get(side));
    }

    @Override
    public boolean isTieBreak() {
        return true;
    }
}
