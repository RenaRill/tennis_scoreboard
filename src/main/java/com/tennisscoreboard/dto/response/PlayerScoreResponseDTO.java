package com.tennisscoreboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerScoreResponseDTO {
    private String name;
    private String points;
    private int games;
    private int sets;
    private Integer tieBreakPoints;
}
