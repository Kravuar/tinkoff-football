package net.kravuar.tinkofffootball.domain.model.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ScoreUpdateFormDTO {
    @Min(0)
    private int team1Score;
    @Min(0)
    private int team2Score;
}
