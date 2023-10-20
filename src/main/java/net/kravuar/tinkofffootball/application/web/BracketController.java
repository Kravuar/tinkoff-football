package net.kravuar.tinkofffootball.application.web;

import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.services.BracketService;
import net.kravuar.tinkofffootball.application.services.TournamentService;
import net.kravuar.tinkofffootball.domain.model.dto.BracketUpdateDTO;
import net.kravuar.tinkofffootball.domain.model.dto.GeneralTournamentDTO;
import net.kravuar.tinkofffootball.domain.model.dto.MatchUpdateDTO;
import net.kravuar.tinkofffootball.domain.model.dto.TournamentFormDTO;
import net.kravuar.tinkofffootball.domain.model.events.TournamentEvent;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/tournaments/{id}/bracket")
@RequiredArgsConstructor
public class BracketController {
    private final BracketService bracketService;

    @PostMapping("/matchUpdate")
    public void postMatchUpdate(@PathVariable Long id, MatchUpdateDTO matchUpdate) {
        bracketService.scoreUpdate(id, matchUpdate);
    }

    @PostMapping("/bracketUpdate")
    public void postBracketUpdate(@PathVariable Long id, BracketUpdateDTO bracketUpdate) {
        bracketService.bracketUpdate(id, bracketUpdate);
    }
}
