package com.tennisscoreboard.model;

import com.tennisscoreboard.exception.InvalidValueException;
import lombok.Getter;

import java.util.Objects;

@Getter
public class OngoingMatch {
    private static final int MIN_GAMES_FOR_WIN = 6;
    private static final int SETS_TO_WIN = 2;
    private static final int MIN_TIE_BREAK_POINTS_FOR_WIN = 7;
    private static final int MIN_MARGIN = 2;

    private final PlayerScore firstPlayerScore;
    private final PlayerScore secondPlayerScore;
    private String winnerName;

    public OngoingMatch(String firstPlayerName, String secondPlayerName) {
        this.firstPlayerScore = new PlayerScore(firstPlayerName);
        this.secondPlayerScore = new PlayerScore(secondPlayerName);
    }

    public String getFirstPlayerName() {
        return firstPlayerScore.getName();
    }

    public String getSecondPlayerName() {
        return secondPlayerScore.getName();
    }

    public void addPoint(String name) {
        PlayerScore player = getPlayerScore(name);
        if (player.isTieBreak()) {
            addTieBreakPoint(player);
        } else {
            Points points = player.getPoints();
            if (points == Points.ADVANTAGE) {
                addGame(player);
            } else if (points == Points.FORTY) {
                checkOpponentAdvantage(player);
            } else {
                player.addPoint(points);
            }
        }
    }

    public void addGame(PlayerScore player) {
        PlayerScore opponent = findOpponent(player);

        int playerGames = player.addAndGetGame();
        int opponentGames = opponent.getGameScore();

        player.resetPoints();
        opponent.resetPoints();

        if (playerGames == MIN_GAMES_FOR_WIN && opponentGames == MIN_GAMES_FOR_WIN) {
            player.startTieBreak();
            opponent.startTieBreak();
        }
        if (playerGames >= MIN_GAMES_FOR_WIN && playerGames - opponentGames >= MIN_MARGIN) {
            addSet(player);
        }
    }

    public void addSet(PlayerScore player) {
        int playerSets = player.addAndGetSet();
        PlayerScore opponent = findOpponent(player);

        player.resetGames();
        opponent.resetGames();

        if (playerSets == SETS_TO_WIN) {
            this.winnerName = player.getName();
        }
    }

    public void addTieBreakPoint(PlayerScore player) {
        PlayerScore opponent = findOpponent(player);

        int playerTieBreak = player.addAndGetTieBreakPoint();
        int opponentTieBreak = opponent.getTieBreakScore();

        if (playerTieBreak >= MIN_TIE_BREAK_POINTS_FOR_WIN && playerTieBreak - opponentTieBreak >= MIN_MARGIN) {
            player.endTieBreak();
            opponent.endTieBreak();
            addSet(player);
        }
    }

    public boolean isCompleted() {
        return getWinnerName() != null;
    }

    public PlayerScore getPlayerScore(String name) {
        if (Objects.equals(name, getFirstPlayerName())) {
            return firstPlayerScore;
        } else if (Objects.equals(name, getSecondPlayerName())) {
            return secondPlayerScore;
        }
        throw new InvalidValueException("Игрок с таким именем не найден.");
    }

    private PlayerScore findOpponent(PlayerScore player) {
        if (Objects.equals(player.getName(), getFirstPlayerName())) {
            return secondPlayerScore;
        } else if (Objects.equals(player.getName(), getSecondPlayerName())) {
            return firstPlayerScore;
        }
        throw new InvalidValueException("Игрок с таким именем не найден.");
    }

    private void checkOpponentAdvantage(PlayerScore player) {
        PlayerScore opponent = findOpponent(player);
        if (opponent.getPoints() == Points.ADVANTAGE) {
            opponent.removeAdvantage();
        } else if (opponent.getPoints() == Points.FORTY) {
            player.setAdvantage();
        } else {
            addGame(player);
        }
    }
}
