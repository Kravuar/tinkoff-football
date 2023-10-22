package net.kravuar.tinkofffootball.domain.model.dto;

import lombok.Data;
import net.kravuar.tinkofffootball.domain.model.tournaments.Tournament;

@Data
public class GeneralTournamentDTO {
    private Long id;
    private String title;
    private int participantsCount;
    private int maxParticipants;
    private Tournament.TournamentStatus status;
    private UserInfoDTO owner;

    //    TODO: Add denormalized data like amount of participants (DONE), prize pool ...

    public GeneralTournamentDTO(Tournament tournament) {
        this.participantsCount = tournament.getParticipants();
        this.maxParticipants = tournament.getMaxParticipants();
        this.id = tournament.getId();
        this.title = tournament.getTitle();
        this.status = tournament.getStatus();
        this.owner = new UserInfoDTO(tournament.getOwner());
    }
}
