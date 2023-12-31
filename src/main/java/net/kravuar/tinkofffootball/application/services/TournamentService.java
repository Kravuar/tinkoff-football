package net.kravuar.tinkofffootball.application.services;

import net.kravuar.tinkofffootball.application.repo.TournamentParticipantsRepo;
import net.kravuar.tinkofffootball.application.repo.TournamentRepo;
import net.kravuar.tinkofffootball.domain.model.dto.*;
import net.kravuar.tinkofffootball.domain.model.events.BracketEvent;
import net.kravuar.tinkofffootball.domain.model.events.ScoreUpdateEvent;
import net.kravuar.tinkofffootball.domain.model.exceptions.ResourceNotFoundException;
import net.kravuar.tinkofffootball.domain.model.tournaments.Team;
import net.kravuar.tinkofffootball.domain.model.tournaments.Tournament;
import net.kravuar.tinkofffootball.domain.model.tournaments.TournamentParticipant;
import net.kravuar.tinkofffootball.domain.model.user.UserInfo;
import net.kravuar.tinkofffootball.domain.model.util.ParticipantId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Transactional
public class TournamentService {
    private final Map<Long, SubscribableChannel> activeTournaments;
    private final ApplicationEventPublisher publisher;
    private final TournamentRepo tournamentRepo;
    private final TournamentParticipantsRepo tournamentParticipantsRepo;
    private final TeamService teamService;
    private final MatchService matchService;
    private final UserService userService;

    public TournamentService(ApplicationEventPublisher publisher, TournamentRepo tournamentRepo, TournamentParticipantsRepo tournamentParticipantsRepo, TeamService teamService, MatchService matchService, UserService userService) {
        this.publisher = publisher;
        this.tournamentRepo = tournamentRepo;
        this.tournamentParticipantsRepo = tournamentParticipantsRepo;
        this.teamService = teamService;
        this.matchService = matchService;
        this.userService = userService;
        this.activeTournaments = new ConcurrentHashMap<>(
                tournamentRepo.findAllByStatusIs(Tournament.TournamentStatus.ACTIVE).stream()
                        .collect(Collectors.toMap(Tournament::getId, (tournament) -> MessageChannels.publishSubscribe().getObject()))
        );
    }

    public Flux<ServerSentEvent<BracketEvent>> subscribeToBracketUpdates(Long tournamentId) {
        return Flux.create(sink -> {
            var tournament = activeTournaments.get(tournamentId);
            MessageHandler handler = message -> {
                var gameEvent = (BracketEvent) message.getPayload();
                var event = ServerSentEvent.<BracketEvent>builder()
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
        if (!Objects.equals(team.getCaptain().getId(), userInfo.getId()) && !Objects.equals(team.getSecondPlayer().getId(), userInfo.getId()))
            throw new IllegalArgumentException("Нельзя присоединится к турниру. Вы не состоите в данной команде.");
        var participants = tournament.getTournamentParticipants();
        if (participants.stream().anyMatch(participant -> Objects.equals(participant.getTeam().getId(), teamId)))
            throw new IllegalArgumentException("Команда уже учавствует.");
        tournament.setParticipants(tournament.getParticipants() + 1);
        tournamentRepo.save(tournament);
        tournamentParticipantsRepo.save(new TournamentParticipant(tournament, team));
//        TODO: fire event to lifetime participants update
    }

    public void leaveTournament(Long tournamentId, Long teamId, UserInfo userInfo) {
        var tournament = findOrElseThrow(tournamentId);
        var tournamentStatus = tournament.getStatus();
        if (tournamentStatus != Tournament.TournamentStatus.ACTIVE && tournamentStatus != Tournament.TournamentStatus.PENDING)
            throw new IllegalArgumentException("Нельзя выйти из турнира, он завершён.");
        var team = teamService.findOrElseThrow(teamId);
        if (!Objects.equals(team.getCaptain().getId(), userInfo.getId()) || !Objects.equals(team.getSecondPlayer().getId(), userInfo.getId()))
            throw new IllegalArgumentException("Нельзя выйти из турнира. Вы не состоите в данной команде.");

        var toDelete = new ParticipantId(tournament, team);
        if (tournamentParticipantsRepo.existsById(toDelete)) {
            switch (tournamentStatus) {
                case PENDING -> {
                    tournamentParticipantsRepo.deleteById(toDelete);
                    tournament.setParticipants(tournament.getParticipants() - 1);
                    tournamentRepo.save(tournament);
                }
                case ACTIVE -> {
                    var match = matchService.findActiveMatch(tournamentId, teamId);
                    long winner;
                    if (Objects.equals(team.getId(), match.getTeam1().getId()))
                        winner = match.getTeam2().getId();
                    else
                        winner = match.getTeam1().getId();
                    publisher.publishEvent(new ScoreUpdateEvent(
                            match.getTeam1Score(),
                            match.getTeam2Score(),
                            match.getTournament().getId(),
                            match.getBracketPosition(),
                            winner
                    ));
                }
                default -> throw new IllegalArgumentException("Нельзя выйти из турнира.");
            }
        }
    }

    public void updateScore(Long matchId, ScoreUpdateFormDTO matchUpdate, UserInfo userInfo) {
        var match = matchService.findOrElseThrow(matchId);
        if (match.getTournament().getStatus() != Tournament.TournamentStatus.ACTIVE)
            throw new IllegalArgumentException("Турнир не активен.");
        if (!Objects.equals(match.getTournament().getOwner().getId(), userInfo.getId()))
            throw new AccessDeniedException("Вы не владелец турнира.");
        if (match.getTeam1() == null || match.getTeam2() == null)
            throw new IllegalArgumentException("Матч ещё не начат.");
        if (match.getTeam1Score() > match.getBestOf() / 2 || match.getTeam2Score() > match.getBestOf() / 2)
            throw new IllegalArgumentException("Матч уже завершён.");
        match.setTeam1Score(matchUpdate.getTeam1Score());
        match.setTeam2Score(matchUpdate.getTeam2Score());
        long winner = -1;
        if (match.getTeam1Score() > match.getBestOf() / 2)
            winner = match.getTeam1().getId();
        else if (match.getTeam2Score() > match.getBestOf() / 2)
            winner = match.getTeam2().getId();
        publisher.publishEvent(new ScoreUpdateEvent(
                match.getTeam1Score(),
                match.getTeam2Score(),
                match.getTournament().getId(),
                match.getBracketPosition(),
                winner
        ));
    }

    public void startTournament(Long tournamentId, UserInfo userInfo) {
        var tournament = findOrElseThrow(tournamentId);
        if (!Objects.equals(tournament.getOwner().getId(), userInfo.getId()))
            throw new AccessDeniedException("Вы не владелец турнира.");
        if (tournament.getStatus() != Tournament.TournamentStatus.PENDING)
            throw new IllegalArgumentException("Нельзя начать турнир, он либо уже активен, либо завершён.");

        var participants = tournamentParticipantsRepo.findAllByTournamentId(tournamentId);
        var participantsCount = participants.size();
        if (participantsCount % 2 == 1)
            participants.add(new TournamentParticipant()); // last match second team

        if (participantsCount < 2)
            throw new IllegalArgumentException("Недостаточно команд для старта турнира.");
        var matches = matchService.getFirstMatches(tournamentId, Pageable.ofSize((int) Math.ceil((double) participantsCount / 2)).first());
        for (var i = 0; i < participantsCount; i += 2) {
            var match = matches.get(i / 2);
            match.setTeam1(participants.get(i).getTeam());
            match.setTeam2(participants.get(i + 1).getTeam());
        }

        matchService.saveAll(matches);
        tournament.setStatus(Tournament.TournamentStatus.ACTIVE);
        tournamentRepo.save(tournament);
        activeTournaments.put(tournamentId, MessageChannels.publishSubscribe().getObject());

        var lastMatch = matches.get(matches.size() - 1);
        if (lastMatch.getTeam2() == null)
            publisher.publishEvent(new ScoreUpdateEvent(
                    0,
                    0,
                    tournamentId,
                    lastMatch.getBracketPosition(),
                    lastMatch.getTeam1().getId()
            ));
    }

    public void cancelTournament(Long tournamentId, UserInfo userInfo) {
        var tournament = findOrElseThrow(tournamentId);
        if (!Objects.equals(tournament.getOwner().getId(), userInfo.getId()))
            throw new AccessDeniedException("Вы не владелец турнира.");
        if (tournament.getStatus() != Tournament.TournamentStatus.ACTIVE && tournament.getStatus() != Tournament.TournamentStatus.PENDING)
            throw new IllegalArgumentException("Турнир нельзя отменить.");
        finishTournament(tournamentId, Tournament.TournamentStatus.CANCELED);
    }

    public void finishTournament(Long tournamentId, Tournament.TournamentStatus status) {
        if (status != Tournament.TournamentStatus.FINISHED && status != Tournament.TournamentStatus.CANCELED)
            throw new IllegalArgumentException("Статус не является завершающим");
        var tournament = findOrElseThrow(tournamentId);
        tournament.setStatus(status);
        tournamentRepo.save(tournament);
        activeTournaments.remove(tournamentId);
    }

    @EventListener(ScoreUpdateEvent.class)
    public void handleScoreUpdate(ScoreUpdateEvent event) {
        if (event.getWinner() != -1)
            matchService.advanceWinner(event.getTournamentId(), event.getBracketPosition(), event.getWinner());
        var channel = activeTournaments.get(event.getTournamentId());
        channel.send(new GenericMessage<>(event));
        if (event.getBracketPosition() == 0 && event.getWinner() != -1)
            finishTournament(event.getTournamentId(), Tournament.TournamentStatus.FINISHED);
    }

    public BracketDTO getBracket(Long tournamentId) {
        return new BracketDTO(findOrElseThrow(tournamentId));
    }

    public TournamentListPageDTO getHistoryTournaments(UserInfo userInfo) {
        return new TournamentListPageDTO(tournamentRepo.findAllByParticipant(userInfo.getId()));
    }

    public TournamentListPageDTO getHistoryTournamentsPageable(UserInfo userInfo, int pageSize, int page) {
        return new TournamentListPageDTO(tournamentRepo.findAllByParticipantPageable(userInfo.getId(), Pageable.ofSize(pageSize).withPage(page)));
    }
}
