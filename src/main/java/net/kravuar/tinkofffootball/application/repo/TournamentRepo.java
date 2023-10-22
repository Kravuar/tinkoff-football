package net.kravuar.tinkofffootball.application.repo;

import net.kravuar.tinkofffootball.domain.model.tournaments.Tournament;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentRepo extends PagingAndSortingRepository<Tournament, Long>, CrudRepository<Tournament, Long> {
    Optional<Tournament> findById(long id);

    List<Tournament> findAllByStatusIs(Tournament.TournamentStatus status);
}
