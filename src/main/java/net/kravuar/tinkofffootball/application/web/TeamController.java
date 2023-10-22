package net.kravuar.tinkofffootball.application.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Команды пользователя.", description = "Возвращает все команды пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Команды получена."),
            @ApiResponse(responseCode = "403", description = "Не авторизован."),
    })
    @GetMapping("/my")
    public Collection<TeamInfoDTO> getMyTeams(@AuthenticationPrincipal UserInfo userInfo) {
        return teamService.getAllTeams(userInfo.getId());
    }

    @Operation(summary = "Информация о команде.", description = "Возвращает информацию о команде.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация получена."),
            @ApiResponse(responseCode = "400", description = "Команда не найдена."),
    })
    @GetMapping("/{id}/info")
    public TeamInfoDTO getTeamInfo(@PathVariable Long id) {
        return teamService.getTeamInfo(id);
    }

    @Operation(summary = "Создать команду.", description = "Создаёт команду и приглашает выбранного напарника.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Команда создана."),
            @ApiResponse(responseCode = "403", description = "Не авторизован."),
            @ApiResponse(responseCode = "400", description = "Напарник не найден."),
    })
    @PostMapping("/create")
    public void createTeam(@RequestBody @Valid TeamFormDTO teamForm, @AuthenticationPrincipal UserInfo userInfo) {
        teamService.createTeam(teamForm, userInfo);
    }

    @Operation(summary = "Принять приглашение в команду.", description = "Войти в команду.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Приглашение принято."),
            @ApiResponse(responseCode = "403", description = "Не авторизован."),
            @ApiResponse(responseCode = "400", description = "Нельзя войти в команду."),
    })
    @PutMapping("/{id}/join/")
    public void joinTeam(@PathVariable Long id, @AuthenticationPrincipal UserInfo userInfo) {
        teamService.joinTeam(id, userInfo);
    }

    @Operation(summary = "Выйти из команды.", description = "Расформировывает команду")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Команда расформирована."),
            @ApiResponse(responseCode = "403", description = "Не авторизован."),
            @ApiResponse(responseCode = "400", description = "Команда не найдена."),
    })
    @DeleteMapping("/{id}/leave/")
    public void leaveTeam(@PathVariable Long id, @AuthenticationPrincipal UserInfo userInfo) {
        teamService.disableTeam(id, userInfo);
    }

    @Operation(summary = "Отклонить приглашение в команду.", description = "Расформировывает команду")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Команда расформирована."),
            @ApiResponse(responseCode = "403", description = "Не авторизован."),
            @ApiResponse(responseCode = "400", description = "Команда не найдена."),
    })
    @DeleteMapping("/{id}/declineInvite/")
    public void declineInvite(@PathVariable Long id, @AuthenticationPrincipal UserInfo userInfo) {
        teamService.deleteTeam(id, userInfo);
    }
}
