package net.kravuar.tinkofffootball.application.repo;

import net.kravuar.tinkofffootball.domain.model.tournaments.Tournament;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentRepo extends PagingAndSortingRepository<Tournament, Long>, CrudRepository<Tournament, Long> {
    Optional<Tournament> findById(long id);

    List<Tournament> findAllByStatusIs(Tournament.TournamentStatus status);

    @Query("SELECT t From TournamentParticipant tp " +
           "JOIN Tournament t ON t.id = tp.tournament.id " +
           "WHERE tp.team.captain.id = ?1 or tp.team.secondPlayer.id = ?1"
    )
    List<Tournament> findAllByParticipant(Long participantId);

    @Query("SELECT t From TournamentParticipant tp " +
            "JOIN Tournament t ON t.id = tp.tournament.id " +
            "WHERE tp.team.captain.id = ?1 or tp.team.secondPlayer.id = ?1 "
    )
    Page<Tournament> findAllByParticipantPageable(Long participantId, Pageable pageable);
}
