package net.kravuar.tinkofffootball.domain.model.tournaments;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.kravuar.tinkofffootball.domain.model.service.Team;

@Entity
@Getter
@Setter
@Table(name="matches")
public class Match {
    public enum MatchStatus {
        PENDING,
        ACTIVE,
        FINISHED
    }

    @Id
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Tournament tournament;

    @Column(nullable = false)
    private int bracketPosition;

    @ManyToOne
    private Team team1;
    @ManyToOne
    private Team team2;

    private int team1Score = 0;
    private int team2Score = 0;

    @Column(nullable = false)
    private int bestOf;

    private String prize;

    @Enumerated
    private MatchStatus status = MatchStatus.PENDING;

    public Match(Tournament tournament, int bracketPosition, int bestOf, String prize) {
        this.tournament = tournament;
        this.bracketPosition = bracketPosition;
        this.prize = prize;
        this.bestOf = bestOf;
    }
}
