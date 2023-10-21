package net.kravuar.tinkofffootball.application.repo;

import net.kravuar.tinkofffootball.domain.model.tournaments.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepo extends JpaRepository<Team, Long> {
    List<Team> findAllByCaptainIdOrSecondPlayerId(Long captainId, Long secondPlayerId);
}
