package net.kravuar.tinkofffootball.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchTeamDTO {
    private Long id;
    private String name;
    private int score;
}
