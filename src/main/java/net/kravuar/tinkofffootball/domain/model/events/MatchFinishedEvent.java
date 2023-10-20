package net.kravuar.tinkofffootball.domain.model.events;

import lombok.Getter;

@Getter
public class MatchFinishedEvent extends BracketEvent {
    private long tournamentId;
    private int bracketPosition;
    private int winner;

    public MatchFinishedEvent(String eventType, long tournamentId, int bracketPosition, int winner) {
        super(eventType);
        this.tournamentId = tournamentId;
        this.bracketPosition = bracketPosition;
        this.winner = winner;
    }
}
