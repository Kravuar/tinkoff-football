package net.kravuar.tinkofffootball.application.web;

import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.services.TournamentService;
import net.kravuar.tinkofffootball.domain.model.dto.ScoreUpdateDTO;
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

    /*
     * Events like participants score update, team movement across the bracket
     * */
    @GetMapping("/subscribe/{id}")
    public Flux<ServerSentEvent<BracketEvent>> subscribeToBracketUpdates(@PathVariable Long id) {
        return tournamentService.subscribeToBracketUpdates(id);
    }

    @PostMapping("/updateScore")
    public void updateScore(@PathVariable Long id, ScoreUpdateDTO matchUpdate, @AuthenticationPrincipal UserInfo userInfo) {
        tournamentService.updateScore(id, matchUpdate, userInfo);
    }

    @PostMapping("/disqualifyTeam/{teamId}")
    public void disqualifyTeam(@PathVariable Long id, @PathVariable Long teamId, @AuthenticationPrincipal UserInfo userInfo) {
        tournamentService.leaveTournament(id, teamId, userInfo);
    }
}
