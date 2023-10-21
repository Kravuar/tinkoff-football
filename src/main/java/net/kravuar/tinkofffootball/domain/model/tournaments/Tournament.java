package net.kravuar.tinkofffootball.domain.model.tournaments;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.kravuar.tinkofffootball.domain.model.dto.TournamentFormDTO;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="tournaments")
@NoArgsConstructor
public class Tournament {
    @Id
    private Long id;

    @Column(nullable = false)
    @NotBlank
    @Min(4)
    private String title;

    @OneToMany(mappedBy = "tournament")
    private Set<Match> matches = new HashSet<>();

//    TODO: Add denormalized data like amount of participants, prize pool

    public Tournament(TournamentFormDTO tournamentForm) {
        this.title = tournamentForm.getTitle();

        var matchDTOs = tournamentForm.getMatches();
        var totalMatches = matchDTOs.size();
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
