package net.kravuar.tinkofffootball.domain.model.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public abstract class BracketEvent extends TournamentEvent {
    public BracketEvent(String eventType) {
        super(eventType);
    }
}

