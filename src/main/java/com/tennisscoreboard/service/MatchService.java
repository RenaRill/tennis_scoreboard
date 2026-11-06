package com.tennisscoreboard.service;

import com.tennisscoreboard.dao.MatchDAO;
import com.tennisscoreboard.dao.PlayerDAO;
import com.tennisscoreboard.entity.Match;
import com.tennisscoreboard.entity.Player;
import com.tennisscoreboard.infrastructure.OngoingMatchStorage;
import com.tennisscoreboard.model.OngoingMatch;
import com.tennisscoreboard.model.PlayerScore;
import com.tennisscoreboard.util.Pagination;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class MatchService {
    private final PlayerDAO playerDAO;
    private final MatchDAO matchDAO;
    private final OngoingMatchStorage matchStorage;


    public MatchService(PlayerDAO playerDAO, MatchDAO matchDAO, OngoingMatchStorage matchStorage) {
        this.playerDAO = playerDAO;
        this.matchDAO = matchDAO;
        this.matchStorage = matchStorage;
    }

    public UUID startMatch(OngoingMatch ongoingMatch) {
        createOrGetPlayer(ongoingMatch.getFirstPlayerName());
        createOrGetPlayer(ongoingMatch.getSecondPlayerName());
        return matchStorage.addMatch(ongoingMatch);
    }

    public List<Match> getAllMatches(int page) {
        return matchDAO.getAllMatches(page);
    }

    public List<Match> getPlayerMatches(int page, String name) {
        return matchDAO.getPlayerMatches(page, name);
    }

    public int getAllTotalPages() {
        return (int) ((matchDAO.countAllMatches() + Pagination.PAGE_SIZE - 1) / Pagination.PAGE_SIZE);
    }

    public int getPlayerTotalPages(String name) {
        return (int) ((matchDAO.countPlayerMatches(name) + Pagination.PAGE_SIZE - 1) / Pagination.PAGE_SIZE);
    }

    public OngoingMatch addPoint(UUID id, String name) {
        OngoingMatch match = getMatchScore(id).orElseThrow();
        // что тут выбросить, если я уже обрабатываю это в контроллере?
        if (!match.isCompleted()) {
            match.addPoint(name);

            if (match.getWinnerName() != null) {
                matchDAO.saveMatch(
                        createOrGetPlayer(match.getFirstPlayerName()),
                        createOrGetPlayer(match.getSecondPlayerName()),
                        createOrGetPlayer(match.getWinnerName())
                );
            }
        }
        return match;
    }

    public Optional<OngoingMatch> getMatchScore(UUID id) {
        return matchStorage.getMatch(id);
    }

    private Player createOrGetPlayer(String playerName) {
        return playerDAO.getPlayerByName(playerName).orElseGet(() -> playerDAO.savePlayer(playerName));
    }
}
