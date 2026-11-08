package com.tennisscoreboard.service;

import com.tennisscoreboard.dao.PlayerDAO;
import com.tennisscoreboard.entity.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayerService {
    private final PlayerDAO playerDAO;

    public PlayerService(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }

    public Player createOrGetPlayer(String playerName) {
        return playerDAO.getPlayerByName(playerName).orElseGet(() -> playerDAO.savePlayer(playerName));
    }
}
