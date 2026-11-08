package com.tennisscoreboard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateMatchRequestDTO {
    @NotBlank
    @Size(max = 50)
    private String firstPlayerName;
    @NotBlank
    @Size(max = 50)
    private String secondPlayerName;
}
