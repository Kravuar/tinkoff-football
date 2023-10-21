package net.kravuar.tinkofffootball.domain.model.dto;

import lombok.Getter;
import net.kravuar.tinkofffootball.domain.model.tournaments.Tournament;

import java.util.List;

@Getter
public class DetailedTournamentDTO extends GeneralTournamentDTO {

    public DetailedTournamentDTO(Tournament tournament) {
        super(tournament);
    }
}
