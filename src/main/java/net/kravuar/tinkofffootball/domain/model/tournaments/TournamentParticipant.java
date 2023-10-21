package net.kravuar.tinkofffootball.domain.model.tournaments;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.kravuar.tinkofffootball.domain.model.util.ParticipantId;

@Entity
@Getter
@Setter
@Table(name="tournament_participants")
@NoArgsConstructor
@IdClass(ParticipantId.class)
public class TournamentParticipant {
    @Id
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Tournament tournament;

    @Id
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    public TournamentParticipant(Tournament tournament, Team team) {
        this.tournament = tournament;
        this.team = team;
    }
}
