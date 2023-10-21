package net.kravuar.tinkofffootball.domain.model.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class BracketEvent {
    private String eventType;
}

