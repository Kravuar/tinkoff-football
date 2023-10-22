package net.kravuar.tinkofffootball.application.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.services.TournamentService;
import net.kravuar.tinkofffootball.domain.model.dto.DetailedTournamentDTO;
import net.kravuar.tinkofffootball.domain.model.dto.TournamentFormDTO;
import net.kravuar.tinkofffootball.domain.model.dto.TournamentListPageDTO;
import net.kravuar.tinkofffootball.domain.model.user.UserInfo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
@Validated
public class TournamentController {
    private final TournamentService tournamentService;

    @Operation(summary = "Получить подробную информацию о турнире.", description = "Подробная информация о турнире")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация получена."),
            @ApiResponse(responseCode = "400", description = "Турнир не найден."),
    })
    @GetMapping("/{id}")
    public DetailedTournamentDTO getTournamentInfo(@PathVariable Long id) {
        return tournamentService.getTournamentDetailed(id);
    }

    @Operation(summary = "Список турниров с пагинацией.", description = "Получить страницу турниров.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Страница получена."),
            @ApiResponse(responseCode = "400", description = "Неправильные параметры пагинации."),
    })
    @GetMapping("/list/{pageSize}/{page}")
    public TournamentListPageDTO getTournamentList(@PathVariable @Min(1) int pageSize, @PathVariable @Min(0) int page) {
        return tournamentService.getTournamentList(pageSize, page);
    }

    @Operation(summary = "Вся история турниров.", description = "Возвращает историю текущего пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "История получена."),
            @ApiResponse(responseCode = "403", description = "Не авторизован."),
    })
    @GetMapping("/myHistory")
    public TournamentListPageDTO getHistoryTournamentList(@AuthenticationPrincipal UserInfo userInfo) {
        return tournamentService.getHistoryTournaments(userInfo);
    }

    @Operation(summary = "История турниров с пагинацией.", description = "Возвращает историю текущего пользователя с пагинацией.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Страница получена."),
            @ApiResponse(responseCode = "403", description = "Не авторизован."),
    })
    @GetMapping("/myHistory/{pageSize}/{page}")
    public TournamentListPageDTO getHistoryTournamentList(@AuthenticationPrincipal UserInfo userInfo, @PathVariable @Min(1) int pageSize, @PathVariable @Min(0) int page) {
        return tournamentService.getHistoryTournamentsPageable(userInfo, pageSize, page);
    }

    @Operation(summary = "Создать турнир.", description = "Создаёт турнир")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Турнир создан."),
            @ApiResponse(responseCode = "403", description = "Не авторизован."),
            @ApiResponse(responseCode = "400", description = "Невозможно создать турнир."),
    })
    @PostMapping("/create")
    public void createTournament(@RequestBody @Valid TournamentFormDTO tournamentForm, @AuthenticationPrincipal UserInfo userInfo) {
        tournamentService.createTournament(tournamentForm, userInfo);
    }

    @Operation(summary = "Начать турнир.", description = "Переводит турнир в активное состояние. Нужно быть владельцем турнира.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Турнир начат."),
            @ApiResponse(responseCode = "403", description = "Не владелец / не авторизован."),
            @ApiResponse(responseCode = "400", description = "Нельзя начать турнир."),
    })
    @PostMapping("/{id}/start")
    public void startTournament(@PathVariable Long id, @AuthenticationPrincipal UserInfo userInfo) {
        tournamentService.startTournament(id, userInfo);
    }

    @Operation(summary = "Зарегистрироваться на турнир.", description = "Регистритует выбранную пользователя с выбранной командой на турнир.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Команда зарегистрирована."),
            @ApiResponse(responseCode = "403", description = "Не авторизован."),
            @ApiResponse(responseCode = "400", description = "Нельзя зарегистрироваться на турнир."),
    })
    @GetMapping("/{tournamentId}/join/{teamId}")
    public void joinTournament(@PathVariable Long tournamentId, @PathVariable Long teamId, @AuthenticationPrincipal UserInfo userInfo) {
        tournamentService.joinTournament(tournamentId, teamId, userInfo);
    }

    @Operation(summary = "Выйти из турнира.", description = "Покинуть турнир")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Регистрация снята (если турнир не начат) либо команда дисквалифицирована (если начат)."),
            @ApiResponse(responseCode = "403", description = "Не авторизован."),
            @ApiResponse(responseCode = "400", description = "Команда / турнир не найдены. Либо невозможно выйти из турнира."),
    })
    @GetMapping("/{tournamentId}/leave/{teamId}")
    public void leaveTournament(@PathVariable Long tournamentId, @PathVariable Long teamId, @AuthenticationPrincipal UserInfo userInfo) {
        tournamentService.leaveTournament(tournamentId, teamId, userInfo);
    }

    @Operation(summary = "Отменить турнир.", description = "Отменяет турнир. Нужно быть владельцем.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Турнир отменён."),
            @ApiResponse(responseCode = "403", description = "Не владелец / не авторизован."),
            @ApiResponse(responseCode = "400", description = "Турнир не найден / нельзя отменить турнир."),
    })
    @PutMapping("/{tournamentId}/cancel/")
    public void cancelTournament(@PathVariable Long tournamentId, @AuthenticationPrincipal UserInfo userInfo) {
        tournamentService.cancelTournament(tournamentId, userInfo);
    }
}
