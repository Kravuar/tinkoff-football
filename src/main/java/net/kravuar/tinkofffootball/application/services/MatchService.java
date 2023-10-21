package net.kravuar.tinkofffootball.application.services;

import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.repo.MatchRepo;
import net.kravuar.tinkofffootball.domain.model.exceptions.ResourceNotFoundException;
import net.kravuar.tinkofffootball.domain.model.tournaments.Match;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepo matchRepo;

    public Match findOrElseThrow(Long id) {
        return matchRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("match", "id", id)
        );
    }

    public List<Match> getFirstMatches(Long tournamentId, Pageable pageable) {
        return matchRepo.findAllByTournamentIdOrderByBracketPositionDesc(tournamentId, pageable);
    }

    public Match findActiveMatch(Long tournamentId, Long teamId) {
        return null;
//        TODO: find
    }

    public void advanceWinner(long tournamentId, int bracketPosition, long winner) {
//        (bracketPosition - 1) / 2
    }
}
