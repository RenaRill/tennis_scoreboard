package com.tennisscoreboard.dto.response;

import lombok.Getter;

@Getter
public class ErrorResponseDTO {
    private final String message;

    public ErrorResponseDTO(String message) {
        this.message = message;
    }
}
