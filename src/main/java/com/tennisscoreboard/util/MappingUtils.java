package com.tennisscoreboard.util;

import com.tennisscoreboard.dto.request.CreateMatchRequestDTO;
import com.tennisscoreboard.dto.response.*;
import com.tennisscoreboard.entity.Match;
import com.tennisscoreboard.model.OngoingMatch;
import com.tennisscoreboard.model.PlayerScore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class MappingUtils {

    public OngoingMatch toOngoingMatchModel(CreateMatchRequestDTO createMatchRequestDTO) {
        return new OngoingMatch(
                createMatchRequestDTO.getFirstPlayerName(),
                createMatchRequestDTO.getSecondPlayerName()
        );
    }

    public CompletedMatchResponseDTO toCompletedMatchDTO(Match match) {
        return new CompletedMatchResponseDTO(match.getFirstPlayerName(), match.getSecondPlayerName(), match.getWinnerName());
    }

    public GetCompletedMatchesResponseDTO toCompletedMatchesDTO(
            List<CompletedMatchResponseDTO> matches, int page, int totalPages) {
        return new GetCompletedMatchesResponseDTO(matches, page, totalPages);
    }

    public PlayerScoreResponseDTO toPlayerScoreDTO(PlayerScore playerScore) {
        String points = null;
        String tieBreakPoints = null;
        if (playerScore.tieBreak()) {
            tieBreakPoints = playerScore.points();
        } else {
            points = playerScore.points();
        }
        return new PlayerScoreResponseDTO(
                playerScore.name(),
                points,
                playerScore.games(),
                playerScore.sets(),
                tieBreakPoints
        );
    }

    public GetOngoingMatchResponseDTO toOngoingMatchDTO(OngoingMatch ongoingMatch) {
        return new GetOngoingMatchResponseDTO(
                toPlayerScoreDTO(ongoingMatch.getPlayerScore(ongoingMatch.getFirstPlayerName())),
                toPlayerScoreDTO(ongoingMatch.getPlayerScore(ongoingMatch.getSecondPlayerName())),
                ongoingMatch.getWinnerName().orElse(null)
        );
    }

    public CreateMatchResponseDTO toCreateMatchDTO(UUID id) {
        return new CreateMatchResponseDTO(id);
    }
}
