package com.tennisscoreboard.infrastructure;

import com.tennisscoreboard.model.OngoingMatch;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OngoingMatchStorage {

    private final Map<UUID, OngoingMatch> matches = new ConcurrentHashMap<>();

    public void addNewMatch(OngoingMatch ongoingMatch) {
        matches.put(UUID.randomUUID(), ongoingMatch);
    }

    public Collection<OngoingMatch> getAllMatches() {
        return matches.values();
    }

    public OngoingMatch getMatch(UUID id) {
        return matches.get(id);
    }
}
