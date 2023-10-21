package net.kravuar.tinkofffootball.domain.model.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;
import net.kravuar.tinkofffootball.domain.model.util.OddNumber;

@Data
public class MatchFormDTO {
    private String prize;
    @Min(3)
    @OddNumber
    private int bestOf;
}
