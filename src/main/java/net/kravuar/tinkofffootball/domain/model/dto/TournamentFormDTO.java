package net.kravuar.tinkofffootball.domain.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TournamentFormDTO {
    @NotBlank
    private String title;
    @NotNull
    @Size(min=1)
    private List<MatchFormDTO> matches;
    private LocalDateTime startDateTime;
}
