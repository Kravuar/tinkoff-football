package net.kravuar.tinkofffootball.domain.model.dto;

import lombok.Getter;
import net.kravuar.tinkofffootball.domain.model.tournaments.Tournament;
import org.springframework.data.domain.Page;

import java.util.Collection;

@Getter
public class TournamentListPageDTO {
    private long totalTournaments;
    private int totalPages;
    private Collection<GeneralTournamentDTO> tournaments;

    public TournamentListPageDTO(Page<Tournament> page) {
        this.tournaments = page.getContent().stream()
                .map(tournament -> new GeneralTournamentDTO(tournament))
                .toList();
        this.totalPages = page.getTotalPages();
        this.totalTournaments = page.getTotalElements();
    }
}
