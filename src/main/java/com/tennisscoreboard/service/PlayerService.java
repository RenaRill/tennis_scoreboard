package com.tennisscoreboard.service;

import com.tennisscoreboard.dao.PlayerDAO;
import com.tennisscoreboard.entity.Player;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PlayerService {
    private final PlayerDAO playerDAO;

    public PlayerService(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }

    @Transactional
    public Player createOrGetPlayer(String playerName) {
        return playerDAO.getPlayerByName(playerName).orElseGet(() -> playerDAO.savePlayer(playerName));
    }
}
