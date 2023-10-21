package net.kravuar.tinkofffootball.domain.model.dto;

import lombok.Data;
import net.kravuar.tinkofffootball.domain.model.tournaments.Tournament;

import java.util.List;

@Data
public class BracketDTO {
    private List<MatchDTO> matches;

    public BracketDTO(Tournament tournament) {
        this.matches = tournament.getMatches().stream()
                .map(MatchDTO::new).toList();
    }
}
