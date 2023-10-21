package net.kravuar.tinkofffootball.application.web;

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

    @GetMapping("/{tournamentId}/bracket")
    public BracketDTO getBracket(@PathVariable Long tournamentId) {
        return tournamentService.getBracket(tournamentId);
    }

    /**
     * SSE Подписка на обновления сетки.
     *
     * @param tournamentId id турнира.
     * @return Поток SSE.
     */
    @GetMapping("/{tournamentId}/bracket/subscribe")
    public Flux<ServerSentEvent<BracketEvent>> subscribeToBracketUpdates(@PathVariable Long tournamentId) {
        return tournamentService.subscribeToBracketUpdates(tournamentId);
    }


    /**
     * Обновления счёта матча.
     *
     * @param matchId id матча.
     */
    @PostMapping("/updateScore/{matchId}")
    public void updateScore(@PathVariable Long matchId, @RequestBody @Valid ScoreUpdateFormDTO matchUpdate, @AuthenticationPrincipal UserInfo userInfo) {
        tournamentService.updateScore(matchId, matchUpdate, userInfo);
    }

    /**
     * Дисквалификация команды.
     *
     * @param tournamentId id турнира.
     * @param teamId id команды.
     */
    @PutMapping("/{tournamentId}/bracket/disqualifyTeam/{teamId}")
    public void disqualifyTeam(@PathVariable Long tournamentId, @PathVariable Long teamId, @AuthenticationPrincipal UserInfo userInfo) {
        tournamentService.leaveTournament(tournamentId, teamId, userInfo);
    }
}
