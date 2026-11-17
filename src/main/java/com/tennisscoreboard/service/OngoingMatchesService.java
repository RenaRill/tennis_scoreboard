package com.tennisscoreboard.service;

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
    private final OngoingMatchStorage matchStorage;
    private final CompletedMatchesService completedMatchesService;
    private final MappingUtils mappingUtils;

    public OngoingMatchesService(OngoingMatchStorage matchStorage, CompletedMatchesService completedMatchesService,
                                 MappingUtils mappingUtils) {
        this.matchStorage = matchStorage;
        this.completedMatchesService = completedMatchesService;
        this.mappingUtils = mappingUtils;
    }

    public GetOngoingMatchResponseDTO getMatchScore(UUID id) {
        OngoingMatch match = findMatch(id);
        synchronized (match) {
            return mappingUtils.toOngoingMatchDTO(match);
        }
    }

    public CreateMatchResponseDTO createMatch(CreateMatchRequestDTO requestDTO) {
        OngoingMatch ongoingMatch = mappingUtils.toOngoingMatchModel(requestDTO);
        return mappingUtils.toCreateMatchDTO(matchStorage.addMatch(ongoingMatch));
    }

    public GetOngoingMatchResponseDTO addPoint(UUID id, String name) {
        OngoingMatch match = findMatch(id);
        synchronized (match) {
            match.addPoint(name);

            if (match.isCompleted()) {
                completedMatchesService.saveMatch(
                        match.getFirstPlayerName(),
                        match.getSecondPlayerName(),
                        match.getWinnerName().orElseThrow(
                                () -> new NotFoundException("Матч завершен, но победитель не определен."))
                );
            }
            return mappingUtils.toOngoingMatchDTO(match);
        }
    }

    private OngoingMatch findMatch(UUID id) {
        return matchStorage.getMatch(id).orElseThrow(() -> new NotFoundException("Матч с таким uuid не найден."));
    }
}
