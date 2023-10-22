package net.kravuar.tinkofffootball.domain.model.dto;

import lombok.Getter;
import net.kravuar.tinkofffootball.domain.model.tournaments.Tournament;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

@Getter
public class TournamentListPageDTO {
    private final long totalTournaments;
    private final int totalPages;
    private final Collection<GeneralTournamentDTO> tournaments;

    public TournamentListPageDTO(Page<Tournament> page) {
        this.tournaments = page.getContent().stream()
                .map(GeneralTournamentDTO::new)
                .toList();
        this.totalPages = page.getTotalPages();
        this.totalTournaments = page.getTotalElements();
    }

    public TournamentListPageDTO(List<Tournament> page) {
        this.tournaments = page.stream()
                .map(GeneralTournamentDTO::new)
                .toList();
        this.totalPages = -1;
        this.totalTournaments = -1;
    }
}
