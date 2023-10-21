package net.kravuar.tinkofffootball.application.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.services.TeamService;
import net.kravuar.tinkofffootball.domain.model.dto.TeamFormDTO;
import net.kravuar.tinkofffootball.domain.model.dto.TeamInfoDTO;
import net.kravuar.tinkofffootball.domain.model.user.UserInfo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping("/my")
    public Collection<TeamInfoDTO> getMyTeams(@AuthenticationPrincipal UserInfo userInfo) {
        return teamService.getAllTeams(userInfo.getId());
    }

    @GetMapping("/{id}/info")
    public TeamInfoDTO getTeamInfo(@PathVariable Long id) {
        return teamService.getTeamInfo(id);
    }

    @PostMapping("/create")
    public void createTeam(@RequestBody @Valid TeamFormDTO teamForm, @AuthenticationPrincipal UserInfo userInfo) {
        teamService.createTeam(teamForm, userInfo);
    }

    @PutMapping("/{id}/join/")
    public void joinTeam(@PathVariable Long id, @AuthenticationPrincipal UserInfo userInfo) {
        teamService.joinTeam(id, userInfo);
    }

    @DeleteMapping("/{id}/leave/")
    public void leaveTeam(@PathVariable Long id, @AuthenticationPrincipal UserInfo userInfo) {
        teamService.disableTeam(id, userInfo);
    }

    @DeleteMapping("/{id}/declineInvite/")
    public void declineInvite(@PathVariable Long id, @AuthenticationPrincipal UserInfo userInfo) {
        teamService.deleteTeam(id, userInfo);
    }
}
