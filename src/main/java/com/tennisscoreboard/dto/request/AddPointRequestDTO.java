package com.tennisscoreboard.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddPointRequestDTO {
    @NotBlank
    private String name;
}
