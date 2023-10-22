package net.kravuar.tinkofffootball.domain.model.dto;

import lombok.Getter;
import net.kravuar.tinkofffootball.domain.model.tournaments.Tournament;
import net.kravuar.tinkofffootball.domain.model.tournaments.TournamentParticipant;

import java.util.List;

@Getter
public class DetailedTournamentDTO extends GeneralTournamentDTO {
    private final List<TeamInfoDTO> participants;

    public DetailedTournamentDTO(Tournament tournament) {
        super(tournament);
        this.participants = tournament.getTournamentParticipants().stream()
                .map(TournamentParticipant::getTeam)
                .map(TeamInfoDTO::new)
                .toList();
    }
}
