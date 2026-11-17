package com.tennisscoreboard.model;

import java.util.EnumMap;
import java.util.Map;

public class TennisSet {
    private static final int MIN_GAMES_FOR_WIN = 6;
    private static final int MIN_MARGIN = 2;

    private final Map<PlayerSide, Integer> games = new EnumMap<>(PlayerSide.class);
    private GameState gameState = new StandardGame();

    public TennisSet() {
        games.put(PlayerSide.FIRST, 0);
        games.put(PlayerSide.SECOND, 0);
    }

    public boolean isTieBreak() {
        return gameState.isTieBreak();
    }

    public String getPoints(PlayerSide side) {
        return gameState.getPoints(side);
    }

    public int getGames(PlayerSide side) {
        return games.get(side);
    }

    public boolean addPoint(PlayerSide side) {
        boolean wasTieBreak = isTieBreak();
        if (!gameState.addPoint(side)) {
            return false;
        }
        if (wasTieBreak) {
            return true;
        }
        int newValue = games.merge(side, 1, Integer::sum);
        int opponentGames = games.get(side.getOpponent());
        if (isWin(newValue, opponentGames)) {
            return true;
        }
        gameState = startTieBreak(newValue, opponentGames) ? new TieBreakGame() : new StandardGame();
        return false;
    }

    private boolean isWin(int games, int opponentGames) {
        return games >= MIN_GAMES_FOR_WIN && games - opponentGames >= MIN_MARGIN;
    }

    private boolean startTieBreak(int games, int opponentGames) {
        return games == MIN_GAMES_FOR_WIN && opponentGames == MIN_GAMES_FOR_WIN;
    }
}
