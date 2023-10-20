package net.kravuar.tinkofffootball.domain.model.dto;

import lombok.Getter;
import net.kravuar.tinkofffootball.domain.model.tournaments.Tournament;

@Getter
public class DetailedTournamentDTO extends GeneralTournamentDTO {
    public DetailedTournamentDTO(Tournament tournament) {
        super(tournament);
    }
}
