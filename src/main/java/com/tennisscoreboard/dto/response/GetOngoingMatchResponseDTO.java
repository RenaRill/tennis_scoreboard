package com.tennisscoreboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetOngoingMatchResponseDTO {
    private PlayerScoreResponseDTO firstPlayer;
    private PlayerScoreResponseDTO secondPlayer;
    private String winnerName;
}
