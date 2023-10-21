package net.kravuar.tinkofffootball.application.services;

import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.repo.MatchRepo;
import net.kravuar.tinkofffootball.domain.model.exceptions.ResourceNotFoundException;
import net.kravuar.tinkofffootball.domain.model.tournaments.Match;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepo matchRepo;

    public Match findOrElseThrow(Long id) {
        return matchRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("match", "id", id)
        );
    }
}
