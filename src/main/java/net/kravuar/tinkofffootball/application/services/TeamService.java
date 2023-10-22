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

import java.util.Collection;
import java.util.Objects;

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

    public Team getReference(Long id) {
        return teamRepo.getReferenceById(id);
    }

    public void createTeam(TeamFormDTO teamForm, UserInfo userInfo) {
        var captain = userService.findOrElseThrow(userInfo.getId());
        var secondPlayer = userService.findOrElseThrow(teamForm.getSecondPlayerId());

        var team = new Team();
        team.setName(teamForm.getName());
        team.setCaptain(captain);
        team.setSecondPlayer(secondPlayer);
        teamRepo.save(team);
    }

    public void joinTeam(Long teamId, UserInfo userInfo) {
        var team = findOrElseThrow(teamId);
        if (!Objects.equals(userInfo.getId(), team.getSecondPlayer().getId()))
            throw new AccessDeniedException("У вас нет приглашения в эту команду.");
        team.setSecondPlayerStatus(Team.SecondPlayerStatus.JOINED);
        teamRepo.save(team);
    }

    public void disableTeam(Long teamId, UserInfo userInfo) {
        var team = findOrElseThrow(teamId);
        if (!Objects.equals(userInfo.getId(), team.getCaptain().getId()) || !Objects.equals(userInfo.getId(), team.getSecondPlayer().getId()))
            throw new AccessDeniedException("Это не ваша команда");
        team.setActive(false);
        teamRepo.save(team);
    }

    public void deleteTeam(Long teamId, UserInfo userInfo) {
        var team = findOrElseThrow(teamId);
        if (!Objects.equals(userInfo.getId(), team.getCaptain().getId()) && !Objects.equals(userInfo.getId(), team.getSecondPlayer().getId()))
            throw new AccessDeniedException("Это не ваша команда");
        teamRepo.delete(team);
    }

    public TeamInfoDTO getTeamInfo(Long id) {
        return new TeamInfoDTO(findOrElseThrow(id));
    }

    public Collection<TeamInfoDTO> getAllTeams(Long userId) {
        return teamRepo.findAllByCaptainIdOrSecondPlayerId(userId, userId).stream()
                .map(TeamInfoDTO::new).toList();
    }
}
