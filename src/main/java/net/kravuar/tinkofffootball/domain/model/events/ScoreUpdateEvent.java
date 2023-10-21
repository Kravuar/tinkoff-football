package net.kravuar.tinkofffootball.domain.model.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public class ScoreUpdateEvent extends BracketEvent {
    @JsonIgnore
    private long tournamentId;
    private int bracketPosition;
    private int team1Score;
    private int team2Score;
    private long winner;

    public ScoreUpdateEvent(int team1Score, int team2Score, long tournamentId, int bracketPosition, long winner) {
        super("score-update");
        this.team1Score = team1Score;
        this.team2Score = team2Score;
        this.tournamentId = tournamentId;
        this.bracketPosition = bracketPosition;
        this.winner = winner;
    }
}
