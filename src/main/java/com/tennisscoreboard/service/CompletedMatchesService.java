package com.tennisscoreboard.service;

import com.tennisscoreboard.dao.MatchDAO;
import com.tennisscoreboard.dto.response.CompletedMatchResponseDTO;
import com.tennisscoreboard.dto.response.GetCompletedMatchesResponseDTO;
import com.tennisscoreboard.entity.Match;
import com.tennisscoreboard.util.MappingUtils;
import com.tennisscoreboard.util.Pagination;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class CompletedMatchesService {
    private final MatchDAO matchDAO;
    private final MappingUtils mappingUtils;
    private final PlayerService playerService;

    public CompletedMatchesService(MatchDAO matchDAO, MappingUtils mappingUtils, PlayerService playerService) {
        this.matchDAO = matchDAO;
        this.mappingUtils = mappingUtils;
        this.playerService = playerService;
    }

    @Transactional
    public void saveMatch(String firstPlayerName, String secondPlayerName, String winnerName) {
        matchDAO.saveMatch(
                playerService.createOrGetPlayer(firstPlayerName),
                playerService.createOrGetPlayer(secondPlayerName),
                playerService.createOrGetPlayer(winnerName)
        );
    }

    @Transactional(readOnly = true)
    public GetCompletedMatchesResponseDTO getMatches(int page, String name) {
        List<Match> matches;
        int offset = Pagination.countOffset(page);
        int limit = Pagination.PAGE_SIZE;
        long totalCount;
        if (name != null && !name.isBlank()) {
            matches = matchDAO.getPlayerMatches(name, offset, limit);
            totalCount = matchDAO.countPlayerMatches(name);
        } else {
            matches = matchDAO.getAllMatches(offset, limit);
            totalCount = matchDAO.countAllMatches();
        }

        List<CompletedMatchResponseDTO> matchesDTO = matches.stream()
                .map(mappingUtils::toCompletedMatchDTO)
                .toList();

        return mappingUtils.toCompletedMatchesDTO(matchesDTO, page, Pagination.countTotalPages(totalCount));
    }
}
