package net.kravuar.tinkofffootball.domain.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TeamFormDTO {
    @NotBlank
    @Min(4)
    private String name;
}
