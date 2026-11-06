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
        Integer tieBreakPoints = null;
        if (playerScore.isTieBreak()) {
            tieBreakPoints = playerScore.getTieBreakScore();
        } else {
            points = playerScore.getPointsScore();
        }
        return new PlayerScoreResponseDTO(
                playerScore.getName(),
                points,
                playerScore.getGameScore(),
                playerScore.getSetScore(),
                tieBreakPoints
        );
    }

    public GetOngoingMatchResponseDTO toOngoingMatchDTO(OngoingMatch ongoingMatch) {
        return new GetOngoingMatchResponseDTO(
                toPlayerScoreDTO(ongoingMatch.getFirstPlayerScore()),
                toPlayerScoreDTO(ongoingMatch.getSecondPlayerScore()),
                ongoingMatch.getWinnerName()
        );
    }

    public CreateMatchResponseDTO toCreateMatchDTO(UUID id) {
        return new CreateMatchResponseDTO(id);
    }
}
