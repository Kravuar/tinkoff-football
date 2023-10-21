package net.kravuar.tinkofffootball.application.web;

import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.services.TeamService;
import net.kravuar.tinkofffootball.domain.model.dto.TeamFormDTO;
import net.kravuar.tinkofffootball.domain.model.dto.TeamInfoDTO;
import net.kravuar.tinkofffootball.domain.model.user.UserInfo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping("/{id}/info")
    public TeamInfoDTO getTeamInfo(@PathVariable Long id) {
        return teamService.getTeamInfo(id);
    }

    @PostMapping("/create")
    public void createTeam(TeamFormDTO teamForm, @AuthenticationPrincipal UserInfo userInfo) {
        teamService.createTeam(teamForm, userInfo);
    }

    @PutMapping("/{id}/join/")
    public void joinTeam(@PathVariable Long id, @AuthenticationPrincipal UserInfo userInfo) {
        teamService.joinTeam(id, userInfo);
    }

    @PutMapping("/{id}/leave/")
    public void leaveTeam(@PathVariable Long id, @AuthenticationPrincipal UserInfo userInfo) {
        teamService.leaveTeam(id, userInfo);
    }

    @DeleteMapping("/{id}/delete/")
    public void deleteTeam(@PathVariable Long id, @AuthenticationPrincipal UserInfo userInfo) {
        teamService.deleteTeam(id, userInfo);
    }
}
