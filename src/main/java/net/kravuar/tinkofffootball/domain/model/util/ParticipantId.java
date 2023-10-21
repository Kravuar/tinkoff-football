package net.kravuar.tinkofffootball.domain.model.util;

import lombok.Data;
import net.kravuar.tinkofffootball.domain.model.tournaments.Team;
import net.kravuar.tinkofffootball.domain.model.tournaments.Tournament;

import java.io.Serializable;

@Data
public class ParticipantId implements Serializable {
    private Team team;
    private Tournament tournament;
}
