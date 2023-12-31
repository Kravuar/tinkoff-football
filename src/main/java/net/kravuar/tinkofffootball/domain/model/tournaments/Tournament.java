package net.kravuar.tinkofffootball.domain.model.tournaments;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.kravuar.tinkofffootball.domain.model.dto.TournamentFormDTO;
import net.kravuar.tinkofffootball.domain.model.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="tournaments")
@NoArgsConstructor
public class Tournament {
    public enum TournamentStatus {
        PENDING,
        ACTIVE,
        FINISHED,
        CANCELED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private Set<Match> matches = new HashSet<>();

    @ManyToOne(optional = false)
    private User owner;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime creationDate = LocalDateTime.now();

//    Validate date (after creationDate)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startDate;

    @Enumerated
    @Column(nullable = false)
    private TournamentStatus status = TournamentStatus.PENDING;

//    TODO: Add denormalized data like amount of participants (DONE), prize pool...

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<TournamentParticipant> tournamentParticipants = new ArrayList<>();

    @Column(nullable = false)
    private int participants = 0;

    @Column(nullable = false)
    private int maxParticipants;

    public Tournament(TournamentFormDTO tournamentForm, User owner) {
        this.owner = owner;
        this.title = tournamentForm.getTitle();
        this.startDate = tournamentForm.getStartDateTime();
        var matchDTOs = tournamentForm.getMatches();
        var totalMatches = matchDTOs.size();
        this.maxParticipants = totalMatches + 1;
        this.matches = new HashSet<>(totalMatches);
        for(var i = 0; i < totalMatches; i++) {
            var matchDTO = matchDTOs.get(i);
            matches.add(new Match(
                    this,
                    i,
                    matchDTO.getBestOf(),
                    matchDTO.getPrize()
            ));
        }
    }
}
