package com.tennisscoreboard.service;

import com.tennisscoreboard.dao.MatchDAO;
import com.tennisscoreboard.dto.response.CompletedMatchResponseDTO;
import com.tennisscoreboard.dto.response.GetCompletedMatchesResponseDTO;
import com.tennisscoreboard.entity.Match;
import com.tennisscoreboard.util.MappingUtils;
import com.tennisscoreboard.util.Pagination;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompletedMatchesService {
    private final MatchDAO matchDAO;
    private final MappingUtils mappingUtils;

    public CompletedMatchesService(MatchDAO matchDAO, MappingUtils mappingUtils) {
        this.matchDAO = matchDAO;
        this.mappingUtils = mappingUtils;
    }

    public GetCompletedMatchesResponseDTO getMatches(int page, String name) {
        List<Match> matches;
        long totalCount;

        if (name != null && !name.isBlank()) {
            matches = matchDAO.getPlayerMatches(page, name);
            totalCount = matchDAO.countPlayerMatches(name);
        } else {
            matches = matchDAO.getAllMatches(page);
            totalCount = matchDAO.countAllMatches();
        }

        List<CompletedMatchResponseDTO> matchesDTO = matches.stream()
                .map(mappingUtils::toCompletedMatchDTO)
                .toList();

        return mappingUtils.toCompletedMatchesDTO(matchesDTO, page, Pagination.countTotalPages(totalCount));
    }
}
