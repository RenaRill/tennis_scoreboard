package com.tennisscoreboard.infrastructure;

import com.tennisscoreboard.model.OngoingMatch;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OngoingMatchStorage {

    private final Map<UUID, OngoingMatch> matches = new ConcurrentHashMap<>();

    public UUID addMatch(OngoingMatch ongoingMatch) {
        UUID id = UUID.randomUUID();
        matches.put(id, ongoingMatch);
        return id;
    }

    public Optional<OngoingMatch> getMatch(UUID id) {
        return Optional.ofNullable(matches.get(id));
    }

    public void removeMatch(UUID id) {
        matches.remove(id);
    }
}
