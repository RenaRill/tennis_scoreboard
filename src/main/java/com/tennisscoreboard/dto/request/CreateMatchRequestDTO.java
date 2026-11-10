package com.tennisscoreboard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateMatchRequestDTO {
    @NotBlank
    @Size(max = 50)
    private String firstPlayerName;
    @NotBlank
    @Size(max = 50)
    private String secondPlayerName;
}
