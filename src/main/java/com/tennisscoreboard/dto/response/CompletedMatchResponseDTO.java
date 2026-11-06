package com.tennisscoreboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompletedMatchResponseDTO {
    private String firstPlayerName;
    private String secondPlayerName;
    private String winnerName;
}
