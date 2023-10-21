package net.kravuar.tinkofffootball.domain.model.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public class ScoreUpdateEvent extends BracketEvent {
    @JsonIgnore
    private long tournamentId;
    private long matchId;
    private int team1Score;
    private int team2Score;
    private int winner;

    public ScoreUpdateEvent(int team1Score, int team2Score, long tournamentId, long matchId, int winner) {
        super("score-update");
        this.team1Score = team1Score;
        this.team2Score = team2Score;
        this.tournamentId = tournamentId;
        this.matchId = matchId;
        this.winner = winner;
    }
}
