package com.tennisscoreboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateMatchResponseDTO {
    private UUID id;
}
