package net.kravuar.tinkofffootball.domain.model.dto;

import lombok.Data;
import net.kravuar.tinkofffootball.domain.model.tournaments.Match;

@Data
public class MatchDTO {
    private Long id;
    private int bracketPosition;
    private MatchTeamDTO team1DTO;
    private MatchTeamDTO team2DTO;
    private int bestOf;
    private String prize;

    public MatchDTO(Match match) {
        this.id = match.getId();
        this.bracketPosition = match.getBracketPosition();
        if (match.getTeam1() != null)
            this.team1DTO = new MatchTeamDTO(match.getTeam1().getId(), match.getTeam1().getName(), match.getTeam1Score());
        if (match.getTeam2() != null)
            this.team2DTO = new MatchTeamDTO(match.getTeam2().getId(), match.getTeam2().getName(), match.getTeam2Score());
        this.bestOf = match.getBestOf();
        this.prize = match.getPrize();
    }
}
