package net.kravuar.tinkofffootball.application.services;

import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.repo.TeamRepo;
import net.kravuar.tinkofffootball.domain.model.dto.TeamFormDTO;
import net.kravuar.tinkofffootball.domain.model.dto.TeamInfoDTO;
import net.kravuar.tinkofffootball.domain.model.exceptions.ResourceNotFoundException;
import net.kravuar.tinkofffootball.domain.model.tournaments.Team;
import net.kravuar.tinkofffootball.domain.model.user.UserInfo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final UserService userService;
    private final TeamRepo teamRepo;

    public Team findOrElseThrow(Long id) {
        return teamRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("team", "id", id)
        );
    }

    public void createTeam(TeamFormDTO teamForm, UserInfo userInfo) {
        var captain = userService.findOrElseThrow(userInfo.getId());
        var secondPlayer = userService.findOrElseThrow(teamForm.getSecondPlayerId());

        var team = new Team();
        team.setCaptain(captain);
        team.setSecondPlayer(secondPlayer);
        teamRepo.save(team);
    }

    public void joinTeam(Long teamId, UserInfo userInfo) {
        var team = findOrElseThrow(teamId);
        if (userInfo.getId() != team.getSecondPlayer().getId())
            throw new AccessDeniedException("У вас нет приглашения в эту команду.");
        team.setSecondPlayerStatus(Team.SecondPlayerStatus.JOINED);
    }

    public void deleteTeam(Long teamId, UserInfo userInfo) {
        var team = findOrElseThrow(teamId);
        if (userInfo.getId() != team.getCaptain().getId())
            throw new AccessDeniedException("Это не ваша команда");
        team.setActive(false);
    }

    public TeamInfoDTO getTeamInfo(Long id) {
        return new TeamInfoDTO(findOrElseThrow(id));
    }
}
