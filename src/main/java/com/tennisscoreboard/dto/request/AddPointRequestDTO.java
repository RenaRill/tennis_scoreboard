package com.tennisscoreboard.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddPointRequestDTO {
    @NotBlank
    private String name;
}
