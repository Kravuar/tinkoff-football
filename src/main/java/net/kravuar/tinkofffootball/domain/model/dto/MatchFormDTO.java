package net.kravuar.tinkofffootball.domain.model.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class MatchFormDTO {
    private String prize;
    @Min(3)
//    @Mod2 == 1
    private int bestOf;

//    TODO: Max duration
}
