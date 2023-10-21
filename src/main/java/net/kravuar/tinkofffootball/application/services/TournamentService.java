package net.kravuar.tinkofffootball.application.services;

import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.repo.TournamentParticipantsRepo;
import net.kravuar.tinkofffootball.application.repo.TournamentRepo;
import net.kravuar.tinkofffootball.domain.model.dto.DetailedTournamentDTO;
import net.kravuar.tinkofffootball.domain.model.dto.ScoreUpdateFormDTO;
import net.kravuar.tinkofffootball.domain.model.dto.TournamentFormDTO;
import net.kravuar.tinkofffootball.domain.model.dto.TournamentListPageDTO;
import net.kravuar.tinkofffootball.domain.model.events.BracketEvent;
import net.kravuar.tinkofffootball.domain.model.exceptions.ResourceNotFoundException;
import net.kravuar.tinkofffootball.domain.model.service.TournamentHandler;
import net.kravuar.tinkofffootball.domain.model.tournaments.Team;
import net.kravuar.tinkofffootball.domain.model.tournaments.Tournament;
import net.kravuar.tinkofffootball.domain.model.tournaments.TournamentParticipant;
import net.kravuar.tinkofffootball.domain.model.user.UserInfo;
import net.kravuar.tinkofffootball.domain.model.util.ParticipantId;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.messaging.MessageHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class TournamentService {
    private final Map<Long, TournamentHandler> activeTournaments = new ConcurrentHashMap<>();
    private final TournamentRepo tournamentRepo;
    private final TournamentParticipantsRepo tournamentParticipantsRepo;
    private final TeamService teamService;
    private final MatchService matchService;
    private final UserService userService;

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

    public Tournament findOrElseThrow(long id) {
        return tournamentRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("tournament", "id", id)
        );
    }

    public TournamentListPageDTO getTournamentList(int pageSize, int page) {
        return new TournamentListPageDTO(
                tournamentRepo.findAll(
                        Pageable.ofSize(pageSize).withPage(page)
                )
        );
    }

    public DetailedTournamentDTO getTournamentDetailed(long id) {
        return new DetailedTournamentDTO(findOrElseThrow(id));
    }

    public void createTournament(TournamentFormDTO tournamentForm, UserInfo userInfo) {
        var owner = userService.getReference(userInfo.getId());
        var tournament = new Tournament(tournamentForm, owner);
        tournamentRepo.save(tournament);
    }

    public synchronized void joinTournament(Long tournamentId, Long teamId, UserInfo userInfo) {
        var tournament = findOrElseThrow(tournamentId);
        if (tournament.getStatus() != Tournament.TournamentStatus.PENDING)
            throw new IllegalArgumentException("Нельзя присоединится к турниру.");
        if (tournament.getParticipants() == tournament.getMaxParticipants())
            throw new IllegalArgumentException("Нельзя присоединится к турниру. Мест нет.");
        var team = teamService.findOrElseThrow(teamId);
        if (team.getSecondPlayerStatus() == Team.SecondPlayerStatus.INVITED)
            throw new IllegalArgumentException("Нельзя присоединится к турниру. Комманда не сформирована.");
        if (team.getCaptain().getId() != userInfo.getId() || team.getSecondPlayer().getId() != userInfo.getId())
            throw new IllegalArgumentException("Нельзя присоединится к турниру. Вы не состоите в данной команде.");
        tournament.setParticipants(tournament.getParticipants() + 1);
        tournamentRepo.save(tournament);
        tournamentParticipantsRepo.save(new TournamentParticipant(tournament, team));
//        TODO: fire event to lifetime participants update
    }

    public void leaveTournament(Long tournamentId, Long teamId, UserInfo userInfo) {
        var tournament = findOrElseThrow(tournamentId);
        var tournamentStatus = tournament.getStatus();
        var team = teamService.findOrElseThrow(teamId);
        if (team.getCaptain().getId() != userInfo.getId() || team.getSecondPlayer().getId() != userInfo.getId())
            throw new IllegalArgumentException("Нельзя выйти из турнира. Вы не состоите в данной команде.");
        if (tournamentStatus != Tournament.TournamentStatus.PENDING || tournamentStatus != Tournament.TournamentStatus.ACTIVE)
            throw new IllegalArgumentException("Нельзя выйти из турнира.");
        var toDelete = new ParticipantId(tournament, team);
        if (tournamentParticipantsRepo.existsById(toDelete)) {
            tournamentParticipantsRepo.deleteById(toDelete);
            tournament.setParticipants(tournament.getParticipants() - 1);
            tournamentRepo.save(tournament);
        }
//        TODO: fire MatchFinishedEvent if tournament is active (auto-lose)
    }

    public void updateScore(Long matchId, ScoreUpdateFormDTO matchUpdate, UserInfo userInfo) {
        var match = matchService.findOrElseThrow(matchId);
        if (match.getTournament().getOwner().getId() != userInfo.getId())
            throw new AccessDeniedException("Вы не владелец турнира.");
        match.setTeam1Score(matchUpdate.getTeam1Score());
        match.setTeam2Score(matchUpdate.getTeam2Score());
//        if (match.getTeam1Score() + match.getTeam2Score() == match.getBestOf())
//              fire event MatchFinishedEvent
    }

    public void startTournament(Long tournamentId, UserInfo userInfo) {
        var tournament = findOrElseThrow(tournamentId);
        if (tournament.getOwner().getId() != userInfo.getId())
            throw new AccessDeniedException("Вы не владелец турнира.");

        var participants = tournamentParticipantsRepo.findAllByTournamentId(tournamentId);
        if (participants.size() < 2) {
            throw new IllegalArgumentException("Недостаточно команд для старта турнира.");
        }
        var matches = matchService.getFirstMatches(tournamentId, Pageable.ofSize((int) Math.ceil(participants.size() / 2)).first());
        for (var i = 0; i < participants.size(); i+=2) {
            var match = matches.get(i / 2);
            match.setTeam1(participants.get(i).getTeam());
            match.setTeam1(participants.get(i + 1).getTeam());
        }
//        if (matches.get(matches.size() - 1).getTeam2() == null) {
//            fire event (auto-win)
//        }

        activeTournaments.put(tournamentId, new TournamentHandler(tournament));
    }

//    TODO: handle proper bracket movement (like if no opponent - advance)

//    TODO: add handler for the ScoreUpdateEvent (check if match is finished, if so fire new MatchFinishedEvent)
//    TODO: add handler for the MatchFinishedEvent (check if the tournament is finished)
}
