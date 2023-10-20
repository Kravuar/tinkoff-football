package net.kravuar.tinkofffootball.application.web;

import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.services.TournamentService;
import net.kravuar.tinkofffootball.domain.model.dto.GeneralTournamentDTO;
import net.kravuar.tinkofffootball.domain.model.dto.TournamentFormDTO;
import net.kravuar.tinkofffootball.domain.model.events.TournamentEvent;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Collection;

@RestController
@RequestMapping("/tournaments")
@RequiredArgsConstructor
public class TournamentController {
    private final TournamentService tournamentService;

    @GetMapping("/{pageSize}/{page}")
    public Collection<GeneralTournamentDTO> getTournamentList(@PathVariable int pageSize, @PathVariable int page) {
        return tournamentService.getTournamentList(pageSize, page);
    }

    @PostMapping("/create")
    public void createTournament(@RequestBody TournamentFormDTO tournamentForm) {
        tournamentService.createTournament(tournamentForm);
    }

    @GetMapping("/subscribe/{id}")
    public Flux<ServerSentEvent<TournamentEvent>> subscribeToUpdates(@PathVariable Long id) {
        return tournamentService.subscribeToTournamentUpdates(id);
    }
}
