package net.kravuar.tinkofffootball.domain.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TournamentFormDTO {
    @NotBlank
    private String title;
    private List<MatchFormDTO> matches;
    private LocalDateTime startDateTime;
}
