package net.kravuar.tinkofffootball.application.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.services.TournamentService;
import net.kravuar.tinkofffootball.domain.model.dto.ScoreUpdateFormDTO;
import net.kravuar.tinkofffootball.domain.model.events.BracketEvent;
import net.kravuar.tinkofffootball.domain.model.user.UserInfo;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/tournaments/{id}/bracket")
@RequiredArgsConstructor
public class BracketController {
    private final TournamentService tournamentService;

    /**
     * SSE Подписка на обновления сетки.
     *
     * @param id id турнира.
     * @return Поток SSE.
     */
    @GetMapping("/subscribe")
    public Flux<ServerSentEvent<BracketEvent>> subscribeToBracketUpdates(@PathVariable Long id) {
        return tournamentService.subscribeToBracketUpdates(id);
    }

    /**
     * Обновления счёта матча.
     *
     * @param id id турнира.
     * @param matchId номер матча (внутри сетки).
     */
    @PostMapping("/updateScore/{matchId}")
    public void updateScore(@PathVariable Long id, @PathVariable Long matchId, @RequestBody @Valid ScoreUpdateFormDTO matchUpdate, @AuthenticationPrincipal UserInfo userInfo) {
        tournamentService.updateScore(id, matchId, matchUpdate, userInfo);
    }

    /**
     * Дисквалификация команды.
     *
     * @param id id турнира.
     * @param teamId id команды.
     */
    @PutMapping("/disqualifyTeam/{teamId}")
    public void disqualifyTeam(@PathVariable Long id, @PathVariable Long teamId, @AuthenticationPrincipal UserInfo userInfo) {
        tournamentService.leaveTournament(id, teamId, userInfo);
    }
}
