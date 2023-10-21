package net.kravuar.tinkofffootball.application.repo;

import net.kravuar.tinkofffootball.domain.model.tournaments.Match;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepo extends PagingAndSortingRepository<Match, Long> ,JpaRepository<Match, Long> {
    List<Match> findAllByTournamentIdOrderByBracketPositionDesc(Long tournamentId, Pageable pageable);
}
