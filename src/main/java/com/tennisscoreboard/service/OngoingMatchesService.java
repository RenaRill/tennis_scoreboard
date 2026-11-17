package com.tennisscoreboard.service;

import com.tennisscoreboard.dto.request.CreateMatchRequestDTO;
import com.tennisscoreboard.dto.response.CreateMatchResponseDTO;
import com.tennisscoreboard.dto.response.GetOngoingMatchResponseDTO;
import com.tennisscoreboard.exception.NotFoundException;
import com.tennisscoreboard.infrastructure.OngoingMatchStorage;
import com.tennisscoreboard.model.OngoingMatch;
import com.tennisscoreboard.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OngoingMatchesService {
    private final OngoingMatchStorage matchStorage;
    private final CompletedMatchesService completedMatchesService;
    private final Mapper mapper;

    public OngoingMatchesService(OngoingMatchStorage matchStorage, CompletedMatchesService completedMatchesService,
                                 Mapper mapper) {
        this.matchStorage = matchStorage;
        this.completedMatchesService = completedMatchesService;
        this.mapper = mapper;
    }

    public GetOngoingMatchResponseDTO getMatchScore(UUID id) {
        OngoingMatch match = findMatch(id);
        synchronized (match) {
            return mapper.toOngoingMatchDTO(match);
        }
    }

    public CreateMatchResponseDTO createMatch(CreateMatchRequestDTO requestDTO) {
        OngoingMatch ongoingMatch = mapper.toOngoingMatchModel(requestDTO);
        return mapper.toCreateMatchDTO(matchStorage.addMatch(ongoingMatch));
    }

    public GetOngoingMatchResponseDTO addPoint(UUID id, String name) {
        OngoingMatch match = findMatch(id);
        synchronized (match) {
            if (match.addPoint(name)) {
                completedMatchesService.saveMatch(
                        match.getFirstPlayerName(),
                        match.getSecondPlayerName(),
                        match.getWinnerName().orElseThrow(
                                () -> new NotFoundException("Матч завершен, но победитель не определен."))
                );
            }
            return mapper.toOngoingMatchDTO(match);
        }
    }

    private OngoingMatch findMatch(UUID id) {
        return matchStorage.getMatch(id).orElseThrow(() -> new NotFoundException("Матч с таким uuid не найден."));
    }
}
