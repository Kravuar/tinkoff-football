package net.kravuar.tinkofffootball.application.services;

import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.repo.TournamentRepo;
import net.kravuar.tinkofffootball.domain.model.dto.DetailedTournamentDTO;
import net.kravuar.tinkofffootball.domain.model.dto.GeneralTournamentDTO;
import net.kravuar.tinkofffootball.domain.model.events.TournamentEvent;
import net.kravuar.tinkofffootball.domain.model.service.TournamentHandler;
import net.kravuar.tinkofffootball.domain.model.tournaments.Tournament;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class TournamentService {
    private final Map<String, TournamentHandler> activeTournaments = new ConcurrentHashMap<>();
    private final TournamentRepo tournamentRepo;

    public  Flux<ServerSentEvent<TournamentEvent>> subscribeToTournamentUpdates(Long tournamentId) {
        return Flux.create(sink -> {
            var tournament = activeTournaments.get(tournamentId);
            MessageHandler handler = message -> {
                var gameEvent = (TournamentEvent) message.getPayload();
                var event = ServerSentEvent.<TournamentEvent> builder()
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

    public Collection<GeneralTournamentDTO> getTournamentList(int pageSize, int page) {
        return tournamentRepo.findAll(Pageable.ofSize(pageSize).withPage(page))
                .getContent().stream()
                .map(tournament -> new GeneralTournamentDTO(tournament))
                .toList();
    }

    public DetailedTournamentDTO getTournamentDetailed(long id) {
        return new DetailedTournamentDTO(tournamentRepo.findById(id));
    }
}
