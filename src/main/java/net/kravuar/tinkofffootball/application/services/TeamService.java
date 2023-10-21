package net.kravuar.tinkofffootball.application.services;

import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.repo.TeamRepo;
import net.kravuar.tinkofffootball.domain.model.dto.TeamFormDTO;
import net.kravuar.tinkofffootball.domain.model.dto.TeamInfoDTO;
import net.kravuar.tinkofffootball.domain.model.exceptions.ResourceNotFoundException;
import net.kravuar.tinkofffootball.domain.model.tournaments.Team;
import net.kravuar.tinkofffootball.domain.model.user.UserInfo;
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
        teamRepo.save(new Team(captain));
    }

    public void inviteToTeam(Long teamId, Long usedId, UserInfo userInfo) {
//        TODO: check if user is invited
    }

    public void joinTeam(Long teamId, UserInfo userInfo) {
//        TODO: check if user is invited
    }

    public void leaveTeam(Long teamId, UserInfo userInfo) {
//        TODO: check if user is in the team
    }

    public void deleteTeam(Long teamId, UserInfo userInfo) {
//        TODO: check if user is in the captain
    }

    public TeamInfoDTO getTeamInfo(Long id) {
        return new TeamInfoDTO(findOrElseThrow(id));
    }
}
