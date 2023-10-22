package net.kravuar.tinkofffootball.domain.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TeamFormDTO {
    @NotBlank
    @Size(min=4, max=100)
    private String name;
    @NotNull
    private Long secondPlayerId;
}
