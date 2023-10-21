package net.kravuar.tinkofffootball.domain.model.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.kravuar.tinkofffootball.domain.model.tournaments.Team;
import net.kravuar.tinkofffootball.domain.model.tournaments.Tournament;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ParticipantId implements Serializable {
    private Tournament tournament;
    private Team team;
}
