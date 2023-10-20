package net.kravuar.tinkofffootball.domain.model.events;

import lombok.Getter;

@Getter
public class ScoreUpdateEvent extends BracketEvent {
    private int team1Score;
    private int team2Score;
    private long tournamentId;

    public ScoreUpdateEvent(String eventType, int team1Score, int team2Score, long tournamentId) {
        super(eventType);
        this.team1Score = team1Score;
        this.team2Score = team2Score;
        this.tournamentId = tournamentId;
    }
}
