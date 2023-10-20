package net.kravuar.tinkofffootball.domain.model.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class TournamentEvent {
    private final String eventType;
}

