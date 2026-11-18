package com.tennisscoreboard.controller;

import com.tennisscoreboard.dto.request.AddPointRequestDTO;
import com.tennisscoreboard.dto.request.CreateMatchRequestDTO;
import com.tennisscoreboard.dto.response.CreateMatchResponseDTO;
import com.tennisscoreboard.dto.response.GetCompletedMatchesResponseDTO;
import com.tennisscoreboard.dto.response.GetOngoingMatchResponseDTO;
import com.tennisscoreboard.service.CompletedMatchesService;
import com.tennisscoreboard.service.OngoingMatchesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/matches")
public class MatchController {
    private final OngoingMatchesService ongoingMatchesService;
    private final CompletedMatchesService completedMatchesService;

    public MatchController(OngoingMatchesService ongoingMatchesService,
                           CompletedMatchesService completedMatchesService) {
        this.ongoingMatchesService = ongoingMatchesService;
        this.completedMatchesService = completedMatchesService;
    }

    @GetMapping
    public ResponseEntity<GetCompletedMatchesResponseDTO> getMatches(
            @RequestParam(name = "page", defaultValue = "1") @Min(1) int page,
            @RequestParam(name = "player_name", required = false) String name) {
        return ResponseEntity.status(HttpStatus.OK).body(
                completedMatchesService.getMatches(page, name)
        );
    }

    @PostMapping
    public ResponseEntity<CreateMatchResponseDTO> createMatch(
            @Valid @RequestBody CreateMatchRequestDTO createMatchRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ongoingMatchesService.createMatch(createMatchRequestDTO)
        );
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<GetOngoingMatchResponseDTO> getOngoingMatchScore(@PathVariable("uuid") UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ongoingMatchesService.getMatchScore(id)
        );
    }

    @PostMapping("/{uuid}/point")
    public ResponseEntity<GetOngoingMatchResponseDTO> addPoint(
            @PathVariable("uuid") UUID id,
            @Valid @RequestBody AddPointRequestDTO request) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ongoingMatchesService.addPoint(id, request.getName())
        );
    }
}
