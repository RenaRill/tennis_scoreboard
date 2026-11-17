package com.tennisscoreboard.model;

import java.util.EnumMap;
import java.util.Map;

public class StandardGame implements GameState {
    private final Map<PlayerSide, Points> points = new EnumMap<>(PlayerSide.class);

    public StandardGame() {
        points.put(PlayerSide.FIRST, Points.LOVE);
        points.put(PlayerSide.SECOND, Points.LOVE);
    }

    @Override
    public boolean addPoint(PlayerSide side) {
        Points playerPoints = points.get(side);
        if (playerPoints == Points.ADVANTAGE) {
            return true;
        }
        if (playerPoints == Points.FORTY) {
            return checkOpponentAdvantage(side);
        }
        points.replace(side, playerPoints.next());
        return false;
    }

    @Override
    public String getPoints(PlayerSide side) {
        return points.get(side).getScore();
    }

    @Override
    public boolean isTieBreak() {
        return false;
    }

    private void removeAdvantage(PlayerSide side) {
        points.replace(side, Points.FORTY);
    }

    private void setAdvantage(PlayerSide side) {
        points.replace(side, Points.ADVANTAGE);
    }

    private boolean checkOpponentAdvantage(PlayerSide side) {
        PlayerSide opponent = side.getOpponent();

        if (points.get(opponent) == Points.ADVANTAGE) {
            removeAdvantage(opponent);
        } else if (points.get(opponent) == Points.FORTY) {
            setAdvantage(side);
        } else {
            return true;
        }
        return false;
    }
}
