package com.tennisscoreboard.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMatchRequestDTO {
    @NotBlank
    private String firstPlayerName;
    @NotBlank
    private String secondPlayerName;
}
