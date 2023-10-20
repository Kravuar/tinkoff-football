package net.kravuar.tinkofffootball.application.repo;

import net.kravuar.tinkofffootball.domain.model.tournaments.Tournament;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepo extends PagingAndSortingRepository<Tournament, Long> {
    Tournament findById(long id);
}
