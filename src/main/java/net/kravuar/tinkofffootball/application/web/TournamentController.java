package net.kravuar.tinkofffootball.application.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.services.TournamentService;
import net.kravuar.tinkofffootball.domain.model.dto.DetailedTournamentDTO;
import net.kravuar.tinkofffootball.domain.model.dto.TournamentFormDTO;
import net.kravuar.tinkofffootball.domain.model.dto.TournamentListPageDTO;
import net.kravuar.tinkofffootball.domain.model.user.UserInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tournaments")
@RequiredArgsConstructor
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

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public void createTournament(@RequestBody @Valid TournamentFormDTO tournamentForm) {
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
