package net.kravuar.tinkofffootball.application.services;

import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.domain.model.dto.BracketUpdateDTO;
import net.kravuar.tinkofffootball.domain.model.dto.MatchUpdateDTO;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BracketService {
    private final TournamentService tournamentService;

    public void scoreUpdate(Long tournamentId, MatchUpdateDTO matchUpdate) {

    }

    public void bracketUpdate(Long tournamentId, BracketUpdateDTO bracketUpdate) {

    }
}
