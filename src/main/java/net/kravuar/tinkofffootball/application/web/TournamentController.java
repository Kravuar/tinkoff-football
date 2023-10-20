package net.kravuar.tinkofffootball.application.web;

import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.services.TournamentService;
import net.kravuar.tinkofffootball.domain.model.dto.TournamentFormDTO;
import net.kravuar.tinkofffootball.domain.model.dto.TournamentListPageDTO;
import net.kravuar.tinkofffootball.domain.model.events.BracketEvent;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/tournaments")
@RequiredArgsConstructor
public class TournamentController {
    private final TournamentService tournamentService;

    @GetMapping("/{pageSize}/{page}")
    public TournamentListPageDTO getTournamentList(@PathVariable int pageSize, @PathVariable int page) {
        return tournamentService.getTournamentList(pageSize, page);
    }

    @PostMapping("/create")
    public void createTournament(@RequestBody TournamentFormDTO tournamentForm) {
        tournamentService.createTournament(tournamentForm);
    }

    @GetMapping("/{tournamentId}/join/{teamId}")
    public void joinTournament(@PathVariable Long tournamentId, @PathVariable Long teamId, @AuthenticationPrincipal UserInfo userInfo) {
        tournamentService.joinTournament(tournamentId, teamId, userInfo);
    }

    @GetMapping("/{tournamentId}/leave/{teamId}")
    public void leaveTournament(@PathVariable Long tournamentId, @PathVariable Long teamId, @AuthenticationPrincipal UserInfo userInfo) {
        tournamentService.leaveTournament(tournamentId, teamId, userInfo);
    }
}
