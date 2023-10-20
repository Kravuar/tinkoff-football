package net.kravuar.tinkofffootball.domain.model.dto;

import lombok.Data;
import net.kravuar.tinkofffootball.domain.model.service.Team;

@Data
public class TeamInfoDTO {
    private UserInfoDTO captain;
    private UserInfoDTO secondPlayer;
    private Team.SecondPlayerStatus secondPlayerStatus;

    public TeamInfoDTO(Team team) {
        this.captain = new UserInfoDTO(team.getCaptain());
        if (team.getSecondPlayer() != null)
            this.secondPlayer = new UserInfoDTO(team.getSecondPlayer());
        this.secondPlayerStatus = team.getSecondPlayerStatus();
    }
}
