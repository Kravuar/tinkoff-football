package net.kravuar.tinkofffootball.domain.model.tournaments;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.kravuar.tinkofffootball.domain.model.util.OddNumber;

@Entity
@Getter
@Setter
@Table(name="matches")
@NoArgsConstructor
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Tournament tournament;

    @Column(nullable = false)
    @Min(0)
    private int bracketPosition;

    @ManyToOne
    private Team team1;
    @ManyToOne
    private Team team2;

    @Min(0)
    private int team1Score = 0;
    @Min(0)
    private int team2Score = 0;

    @Column(nullable = false)
    @Min(3)
    @OddNumber
    private int bestOf;

    private String prize;

    public Match(Tournament tournament, int bracketPosition, int bestOf, String prize) {
        this.tournament = tournament;
        this.bracketPosition = bracketPosition;
        this.prize = prize;
        this.bestOf = bestOf;
    }
}
