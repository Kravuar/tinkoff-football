package net.kravuar.tinkofffootball.domain.model.tournaments;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.kravuar.tinkofffootball.domain.model.user.User;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="teams")
@NoArgsConstructor
public class Team {
    public enum SecondPlayerStatus {
        INVITED,
        JOINED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User captain;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User secondPlayer;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    @Enumerated
    private SecondPlayerStatus secondPlayerStatus = SecondPlayerStatus.INVITED;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private Set<TournamentParticipant> tournamentParticipants;
}
