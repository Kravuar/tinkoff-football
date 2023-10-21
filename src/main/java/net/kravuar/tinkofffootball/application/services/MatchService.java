package net.kravuar.tinkofffootball.application.services;

import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.repo.MatchRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepo matchRepo;
}
