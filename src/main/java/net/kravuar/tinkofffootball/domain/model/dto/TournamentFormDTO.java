package net.kravuar.tinkofffootball.domain.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TournamentFormDTO {
    @NotBlank
    private String title;
    @NotNull
    @Min(2)
    private int participants;
    private List<MatchFormDTO> matches;
    private LocalDateTime startDateTime;
}
