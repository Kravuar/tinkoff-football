package net.kravuar.tinkofffootball.domain.model.dto;

import lombok.Data;
import net.kravuar.tinkofffootball.domain.model.tournaments.Team;

@Data
public class TeamInfoDTO {
    private Long id;
    private boolean active;
    private String name;
    private UserInfoDTO captain;
    private UserInfoDTO secondPlayer;
    private Team.SecondPlayerStatus secondPlayerStatus;

    public TeamInfoDTO(Team team) {
        this.id = team.getId();
        this.active = team.getActive();
        this.captain = new UserInfoDTO(team.getCaptain());
        this.name = team.getName();
        if (team.getSecondPlayer() != null)
            this.secondPlayer = new UserInfoDTO(team.getSecondPlayer());
        this.secondPlayerStatus = team.getSecondPlayerStatus();
    }
}
