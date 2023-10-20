package net.kravuar.tinkofffootball.domain.model.dto;

import lombok.Data;
import net.kravuar.tinkofffootball.domain.model.tournaments.Tournament;

@Data
public class GeneralTournamentDTO {
    private Long id;
    private String title;

    //    TODO: Add denormalized data like amount of participants, prize pool

    public GeneralTournamentDTO(Tournament tournament) {
        this.id = tournament.getId();
        this.title = tournament.getTitle();
    }
}
