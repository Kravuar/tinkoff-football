package net.kravuar.tinkofffootball.application.repo;

import net.kravuar.tinkofffootball.domain.model.tournaments.Team;
import net.kravuar.tinkofffootball.domain.model.tournaments.TournamentParticipant;
import net.kravuar.tinkofffootball.domain.model.util.ParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentParticipantsRepo extends JpaRepository<TournamentParticipant, ParticipantId> {
}
