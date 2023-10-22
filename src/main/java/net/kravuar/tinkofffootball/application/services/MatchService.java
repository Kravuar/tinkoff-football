package net.kravuar.tinkofffootball.application.services;

import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.repo.MatchRepo;
import net.kravuar.tinkofffootball.domain.model.exceptions.ResourceNotFoundException;
import net.kravuar.tinkofffootball.domain.model.tournaments.Match;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchService {
    private final MatchRepo matchRepo;
    private final TeamService teamService;

    public Match findOrElseThrow(Long id) {
        return matchRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("match", "id", id)
        );
    }

    public List<Match> getFirstMatches(Long tournamentId, Pageable pageable) {
        return matchRepo.findAllByTournamentIdOrderByBracketPositionDesc(tournamentId, pageable);
    }

    public Match findActiveMatch(Long tournamentId, Long teamId) {
        return matchRepo.findFirstByTournamentIdAndTeam1IdOrTeam2IdOrderByBracketPositionDesc(tournamentId, teamId, teamId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("match", "team", teamId)
                );
    }

    public void saveAll(Iterable<Match> matches) {
        matchRepo.saveAll(matches);
    }

    public void advanceWinner(long tournamentId, int bracketPosition, long winner) {
        var newBracketPosition = (bracketPosition - 1) / 2;
        if (newBracketPosition < 0)
            throw new IllegalArgumentException("Нельзя продвинуться дальше финала.");
        var newMatch = matchRepo.findMatchByTournamentIdAndBracketPosition(tournamentId, newBracketPosition)
                .orElseThrow(
                        () -> new ResourceNotFoundException("match", "bracketPosition", newBracketPosition)
                );
        if (newMatch.getTeam1() == null)
            newMatch.setTeam1(teamService.getReference(winner));
        else if (newMatch.getTeam2() == null)
            newMatch.setTeam2(teamService.getReference(winner));
        else
            throw new IllegalArgumentException("Матч уже сформирован.");
        matchRepo.save(newMatch);
    }
}
