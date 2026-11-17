package com.tennisscoreboard.model;

import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class OngoingMatch {
    private static final int SETS_TO_WIN = 2;

    @Getter
    private final Players players;
    private final Map<PlayerSide, Integer> sets = new EnumMap<>(PlayerSide.class);
    private TennisSet currentSet = new TennisSet();

    public OngoingMatch(String firstPlayerName, String secondPlayerName) {
        players = new Players(firstPlayerName, secondPlayerName);
        sets.put(PlayerSide.FIRST, 0);
        sets.put(PlayerSide.SECOND, 0);
    }

    public boolean addPoint(String playerName) {
        if (isCompleted()) {
            return false;
        }
        PlayerSide playerSide = players.sideOf(playerName);
        if (currentSet.addPoint(playerSide)) {
            sets.merge(playerSide, 1, Integer::sum);
            currentSet = new TennisSet();
        }
        return isCompleted();
    }

    public String getFirstPlayerName() {
        return players.firstPlayerName();
    }

    public String getSecondPlayerName() {
        return players.secondPlayerName();
    }

    public Optional<String> getWinnerName() {
        if (sets.get(PlayerSide.FIRST) == SETS_TO_WIN) {
            return Optional.of(getPlayers().nameOf(PlayerSide.FIRST));
        } else if (sets.get(PlayerSide.SECOND) == SETS_TO_WIN) {
            return Optional.of(getPlayers().nameOf(PlayerSide.SECOND));
        }
        return Optional.empty();
    }

    public boolean isCompleted() {
        return getWinnerName().isPresent();
    }

    public PlayerScore getPlayerScore(String playerName) {
        PlayerSide playerSide = players.sideOf(playerName);
        return new PlayerScore(
                playerName,
                currentSet.getPoints(playerSide),
                currentSet.getGames(playerSide),
                sets.get(playerSide),
                currentSet.isTieBreak()
        );
    }
}
