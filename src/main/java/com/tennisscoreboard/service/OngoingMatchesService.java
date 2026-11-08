package com.tennisscoreboard.service;

import com.tennisscoreboard.dao.MatchDAO;
import com.tennisscoreboard.dto.request.CreateMatchRequestDTO;
import com.tennisscoreboard.dto.response.CreateMatchResponseDTO;
import com.tennisscoreboard.dto.response.GetOngoingMatchResponseDTO;
import com.tennisscoreboard.exception.NotFoundException;
import com.tennisscoreboard.infrastructure.OngoingMatchStorage;
import com.tennisscoreboard.model.OngoingMatch;
import com.tennisscoreboard.util.MappingUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OngoingMatchesService {
    private final MatchDAO matchDAO;
    private final OngoingMatchStorage matchStorage;
    private final PlayerService playerService;
    private final MappingUtils mappingUtils;

    public OngoingMatchesService(MatchDAO matchDAO, OngoingMatchStorage matchStorage, PlayerService playerService, MappingUtils mappingUtils) {
        this.matchDAO = matchDAO;
        this.matchStorage = matchStorage;
        this.playerService = playerService;
        this.mappingUtils = mappingUtils;
    }

    public GetOngoingMatchResponseDTO getMatchScore(UUID id) {
        return mappingUtils.toOngoingMatchDTO(findMatch(id));
    }

    public CreateMatchResponseDTO createMatch(CreateMatchRequestDTO requestDTO) {
        OngoingMatch ongoingMatch = mappingUtils.toOngoingMatchModel(requestDTO);
        playerService.createOrGetPlayer(ongoingMatch.getFirstPlayerName().strip());
        playerService.createOrGetPlayer(ongoingMatch.getSecondPlayerName().strip());
        return mappingUtils.toCreateMatchDTO(matchStorage.addMatch(ongoingMatch));
    }

    public GetOngoingMatchResponseDTO addPoint(UUID id, String name) {
        OngoingMatch match = findMatch(id);
        if (!match.isCompleted()) {
            match.addPoint(name);

            match.getWinnerName().ifPresent(winnerName -> matchDAO.saveMatch(
                    playerService.createOrGetPlayer(match.getFirstPlayerName()),
                    playerService.createOrGetPlayer(match.getSecondPlayerName()),
                    playerService.createOrGetPlayer(winnerName)
            ));

        }
        return mappingUtils.toOngoingMatchDTO(match);
    }

    public OngoingMatch findMatch(UUID id) {
        return matchStorage.getMatch(id).orElseThrow(() -> new NotFoundException("Матч с таким uuid не найден."));
    }
}
