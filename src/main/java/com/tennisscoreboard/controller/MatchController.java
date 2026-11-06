package com.tennisscoreboard.controller;

import com.tennisscoreboard.dto.request.AddPointRequestDTO;
import com.tennisscoreboard.dto.request.CreateMatchRequestDTO;
import com.tennisscoreboard.dto.response.CompletedMatchResponseDTO;
import com.tennisscoreboard.dto.response.CreateMatchResponseDTO;
import com.tennisscoreboard.dto.response.GetCompletedMatchesResponseDTO;
import com.tennisscoreboard.dto.response.GetOngoingMatchResponseDTO;
import com.tennisscoreboard.exception.InvalidValueException;
import com.tennisscoreboard.exception.NotFoundException;
import com.tennisscoreboard.model.OngoingMatch;
import com.tennisscoreboard.service.MatchService;
import com.tennisscoreboard.util.MappingUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/matches")
public class MatchController {
    private final MatchService matchService;
    private final MappingUtils mappingUtils;

    public MatchController(MatchService matchService, MappingUtils mappingUtils) {
        this.matchService = matchService;
        this.mappingUtils = mappingUtils;
    }

    @GetMapping
    public ResponseEntity<GetCompletedMatchesResponseDTO> getMatches(@RequestParam(name = "page", required = false, defaultValue = "1") int page, @RequestParam(name = "name", required = false) String name) {
        List<CompletedMatchResponseDTO> matches;
        int totalPages;
        if (name == null) {
            matches = matchService.getAllMatches(page).stream()
                    .map(mappingUtils::toCompletedMatchDTO)
                    .toList();
            totalPages = matchService.getAllTotalPages();
        } else {
            matches = matchService.getPlayerMatches(page, name).stream()
                    .map(mappingUtils::toCompletedMatchDTO)
                    .toList();
            totalPages = matchService.getPlayerTotalPages(name);
        }
        GetCompletedMatchesResponseDTO matchesResponse = mappingUtils.toCompletedMatchesDTO(matches, page, totalPages);
        return ResponseEntity.status(HttpStatus.OK).body(matchesResponse);
    }

    @PostMapping
    public ResponseEntity<CreateMatchResponseDTO> createMatch(
            @Valid @RequestBody CreateMatchRequestDTO createMatchRequestDTO) {
        if (Objects.equals(createMatchRequestDTO.getFirstPlayerName(), createMatchRequestDTO.getSecondPlayerName())) {
            throw new InvalidValueException("Имена игроков не могут совпадать.");
        }
        // поменять тут все на start вместо create?
        UUID matchId = matchService.startMatch(mappingUtils.toOngoingMatchModel(createMatchRequestDTO));
        CreateMatchResponseDTO matchResponseDTO = mappingUtils.toCreateMatchDTO(matchId);
        return ResponseEntity.status(HttpStatus.CREATED).body(matchResponseDTO);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<GetOngoingMatchResponseDTO> getOngoingMatchScore(@PathVariable("uuid") UUID id) {
        OngoingMatch ongoingMatch = matchService.getMatchScore(id).orElseThrow(() -> new NotFoundException("Матч с таким uuid не найден."));
        GetOngoingMatchResponseDTO match = mappingUtils.toOngoingMatchDTO(ongoingMatch);
        return ResponseEntity.status(HttpStatus.OK).body(match);
    }

    @PostMapping("/{uuid}/point")
    public ResponseEntity<GetOngoingMatchResponseDTO> addPoint(
            @PathVariable("uuid") UUID id,
            @RequestBody AddPointRequestDTO request) {
        matchService.getMatchScore(id).orElseThrow(() -> new NotFoundException("Матч с таким uuid не найден."));
        GetOngoingMatchResponseDTO match = mappingUtils.toOngoingMatchDTO(matchService.addPoint(id, request.getName()));
        return ResponseEntity.status(HttpStatus.OK).body(match);
    }
}
