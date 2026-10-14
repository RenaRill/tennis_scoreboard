package com.tennisscoreboard.model;

public class OngoingMatch {
    private PlayerScore firstPlayerScore;
    private PlayerScore secondPlayerScore;
    private String winnerName;

    public OngoingMatch(String firstPlayerName, String secondPlayerName) {
        this.firstPlayerScore = new PlayerScore(firstPlayerName);
        this.secondPlayerScore = new PlayerScore(secondPlayerName);
        this.winnerName = null;
    }

    public void addPoint(PlayerScore playerScore) {
        Points points = playerScore.getPoints();
        if (points == Points.ADVANTAGE) {
            addGame(playerScore);
        } else {
            playerScore.addPoint(points);
        }
    }

    public void addGame(PlayerScore scorerScore) {
        PlayerScore opponentScore;
        if (scorerScore == firstPlayerScore) {
            opponentScore = secondPlayerScore;
        } else {
            opponentScore = firstPlayerScore;
        }

        scorerScore.addGame(scorerScore.getGames());
        opponentScore.addGame(opponentScore.getGames());

        int scorerGamesScore = scorerScore.getGames().getScore();
        int opponentGamesScore = opponentScore.getGames().getScore();

        if (scorerGamesScore == 6 & opponentGamesScore == 6) {
            startTieBreak(scorerScore, opponentScore);
        }
        if (scorerGamesScore >= 6 & scorerGamesScore - opponentGamesScore >= 2) {
            addSet(scorerScore);
        }
    }

    public void addSet(PlayerScore playerScore) {
        playerScore.addSet(playerScore.getSets());

        if (playerScore.getSets().getScore() == 2) {
            end(playerScore);
            // сохраняем матч
        }
    }

    public void startTieBreak(PlayerScore scorerScore, PlayerScore opponentScore) {
        scorerScore.startTieBreak();
        opponentScore.startTieBreak();
        scorerScore.nullPoints();
        opponentScore.nullPoints();
    }

    public void addTieBreakPoint(PlayerScore scorerScore, PlayerScore opponentScore) {
        scorerScore.addTieBreakPoint(scorerScore.getTieBreakPoints());

        int scorerTieBreakScore = scorerScore.getTieBreakPoints().getScore();
        int opponentTieBreakScore = opponentScore.getTieBreakPoints().getScore();

        if (scorerTieBreakScore >= 7 & scorerTieBreakScore - opponentTieBreakScore >= 2) {
            end(scorerScore);
            // сохраняем матч
        }
    }

    public void end(PlayerScore playerScore) {
        this.winnerName = playerScore.getName();
    }
}
