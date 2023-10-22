package net.kravuar.tinkofffootball.application.web;

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

    @GetMapping("/{id}")
    public DetailedTournamentDTO getTournamentInfo(@PathVariable Long id) {
        return tournamentService.getTournamentDetailed(id);
    }

    @GetMapping("/list/{pageSize}/{page}")
    public TournamentListPageDTO getTournamentList(@PathVariable @Min(1) int pageSize, @PathVariable @Min(0) int page) {
        return tournamentService.getTournamentList(pageSize, page);
    }

    @PostMapping("/create")
    public void createTournament(@RequestBody @Valid TournamentFormDTO tournamentForm, @AuthenticationPrincipal UserInfo userInfo) {
        tournamentService.createTournament(tournamentForm, userInfo);
    }

    @PostMapping("/{id}/start")
    public void startTournament(@PathVariable Long id, @AuthenticationPrincipal UserInfo userInfo) {
        tournamentService.startTournament(id, userInfo);
    }


    @GetMapping("/{tournamentId}/join/{teamId}")
    public void joinTournament(@PathVariable Long tournamentId, @PathVariable Long teamId, @AuthenticationPrincipal UserInfo userInfo) {
        tournamentService.joinTournament(tournamentId, teamId, userInfo);
    }

    @GetMapping("/{tournamentId}/leave/{teamId}")
    public void leaveTournament(@PathVariable Long tournamentId, @PathVariable Long teamId, @AuthenticationPrincipal UserInfo userInfo) {
        tournamentService.leaveTournament(tournamentId, teamId, userInfo);
    }

    @PutMapping("/{tournamentId}/cancel/")
    public void cancelTournament(@PathVariable Long tournamentId, @AuthenticationPrincipal UserInfo userInfo) {
        tournamentService.cancelTournament(tournamentId, userInfo);
    }
}
