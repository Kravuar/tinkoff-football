package net.kravuar.tinkofffootball.application.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.services.TournamentService;
import net.kravuar.tinkofffootball.domain.model.dto.BracketDTO;
import net.kravuar.tinkofffootball.domain.model.dto.ScoreUpdateFormDTO;
import net.kravuar.tinkofffootball.domain.model.events.BracketEvent;
import net.kravuar.tinkofffootball.domain.model.user.UserInfo;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class BracketController {
    private final TournamentService tournamentService;

    @Operation(summary = "Получение сетки", description = "Получить текущее состояние сетки.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно, "),
            @ApiResponse(responseCode = "403", description = "Неправильный пароль"),
    })
    @GetMapping("/{tournamentId}/bracket")
    public BracketDTO getBracket(@PathVariable Long tournamentId) {
        return tournamentService.getBracket(tournamentId);
    }

    @Operation(summary = "Подписка на SSE обновления сетки", description = "Открывает SSE поток для ивентов обновления сетки.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поток открыт."),
            @ApiResponse(responseCode = "403", description = "Неправильный пароль"),
    })
    @GetMapping("/{tournamentId}/bracket/subscribe")
    public Flux<ServerSentEvent<BracketEvent>> subscribeToBracketUpdates(@PathVariable Long tournamentId) {
        return tournamentService.subscribeToBracketUpdates(tournamentId);
    }

    @Operation(summary = "Обновление счёта матча.", description = "Обновляет счёт выбранного матча, необходимо быть владельцем турнира.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Счёт обновлён."),
            @ApiResponse(responseCode = "403", description = "Вы не владелец / не авторизован."),
            @ApiResponse(responseCode = "400", description = "Невозможно обновить счёт."),
    })
    @PostMapping("/updateScore/{matchId}")
    public void updateScore(@PathVariable Long matchId, @RequestBody @Valid ScoreUpdateFormDTO matchUpdate, @AuthenticationPrincipal UserInfo userInfo) {
        tournamentService.updateScore(matchId, matchUpdate, userInfo);
    }

    @Operation(summary = "Дисквалификация команды.", description = "Засчитывает авто-поражение выбранной команде. Нужно быть владельцем турнира.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Команда успешно дисквалифицирована."),
            @ApiResponse(responseCode = "403", description = "Вы не владелец / не авторизован."),
            @ApiResponse(responseCode = "400", description = "Невозможно дисквалифицировать команду."),
    })
    @PutMapping("/{tournamentId}/bracket/disqualifyTeam/{teamId}")
    public void disqualifyTeam(@PathVariable Long tournamentId, @PathVariable Long teamId, @AuthenticationPrincipal UserInfo userInfo) {
        tournamentService.leaveTournament(tournamentId, teamId, userInfo);
    }
}
