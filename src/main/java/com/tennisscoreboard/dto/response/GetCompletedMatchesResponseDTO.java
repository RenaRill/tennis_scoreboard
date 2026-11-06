package com.tennisscoreboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetCompletedMatchesResponseDTO {
    private List<CompletedMatchResponseDTO> matches;
    private int currentPage;
    private int totalPages;
}
