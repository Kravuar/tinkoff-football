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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Tournament tournament;

    @Column(nullable = false)
    private int bracketPosition;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team1;
    @ManyToOne(fetch = FetchType.LAZY)
    private Team team2;

    private int team1Score;
    private int team2Score;

    @Column(nullable = false)
    private int bestOf;

    private String prize;

    @Enumerated
    private MatchStatus status;
}
