package com.tennisscoreboard.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "matches")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "player1_id", nullable = false)
    private Player firstPlayer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "player2_id", nullable = false)
    private Player secondPlayer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id", nullable = false)
    private Player winner;

    public Match(Player firstPlayer, Player secondPlayer, Player winner) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.winner = winner;
    }

    public String getFirstPlayerName() {
        return firstPlayer.getName();
    }

    public String getSecondPlayerName() {
        return secondPlayer.getName();
    }

    public String getWinnerName() {
        return winner.getName();
    }
}
