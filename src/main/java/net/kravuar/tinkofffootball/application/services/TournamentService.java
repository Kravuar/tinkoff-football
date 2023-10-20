package net.kravuar.tinkofffootball.application.services;

import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.repo.TournamentRepo;
import net.kravuar.tinkofffootball.domain.model.dto.DetailedTournamentDTO;
import net.kravuar.tinkofffootball.domain.model.dto.ScoreUpdateDTO;
import net.kravuar.tinkofffootball.domain.model.dto.TournamentFormDTO;
import net.kravuar.tinkofffootball.domain.model.dto.TournamentListPageDTO;
import net.kravuar.tinkofffootball.domain.model.events.BracketEvent;
import net.kravuar.tinkofffootball.domain.model.service.TournamentHandler;
import net.kravuar.tinkofffootball.domain.model.tournaments.Tournament;
import net.kravuar.tinkofffootball.domain.model.user.UserInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class TournamentService {
    private final Map<String, TournamentHandler> activeTournaments = new ConcurrentHashMap<>();
    private final TournamentRepo tournamentRepo;

    public Flux<ServerSentEvent<BracketEvent>> subscribeToBracketUpdates(Long tournamentId) {
        return Flux.create(sink -> {
            var tournament = activeTournaments.get(tournamentId);
            MessageHandler handler = message -> {
                var gameEvent = (BracketEvent) message.getPayload();
                var event = ServerSentEvent.<BracketEvent> builder()
                        .event(gameEvent.getEventType())
                        .data(gameEvent)
                        .build();
                sink.next(event);
            };
            try {
                tournament.subscribe(handler);
            } catch (Exception subscriptionFailure) {
                sink.error(subscriptionFailure);
            }
        }, FluxSink.OverflowStrategy.LATEST);
    }

    public TournamentListPageDTO getTournamentList(int pageSize, int page) {
        return new TournamentListPageDTO(
                tournamentRepo.findAll(
                        Pageable.ofSize(pageSize).withPage(page)
                )
        );
    }

    public DetailedTournamentDTO getTournamentDetailed(long id) {
        return new DetailedTournamentDTO(tournamentRepo.findById(id));
    }

    public void createTournament(TournamentFormDTO tournamentForm) {
        var tournament = new Tournament(tournamentForm);
        tournamentRepo.save(tournament);
    }

    public void joinTournament(Long tournamentId, Long teamId, UserInfo userInfo) {
//        TODO: check whether team has 2 players and the userInfo principal is the captain
//        TODO: check whether tournament isn't full or add check in functionality
//        TODO: join the tournament
//        TODO: fire event to lifetime participants update
    }

    public void leaveTournament(Long tournamentId, Long teamId, UserInfo userInfo) {
//        TODO: check whether userInfo principal is the captain / owner of the tournament
//        TODO: fire MatchFinishedEvent (auto-lose)
    }

    public void updateScore(Long tournamentId, ScoreUpdateDTO matchUpdate, UserInfo userInfo) {
//  TODO: Check whether userInfo principal is the owner of this tournament

    }

//    TODO: handle proper bracket movement (like if no opponent - advance)

//    TODO: add handler for the ScoreUpdateEvent (check if match is finished, if so fire new MatchFinishedEvent)
//    TODO: add handler for the MatchFinishedEvent (check if the tournament is finished)
}
