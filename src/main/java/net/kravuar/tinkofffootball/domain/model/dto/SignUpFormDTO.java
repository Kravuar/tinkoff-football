package net.kravuar.tinkofffootball.domain.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignUpFormDTO {
    @NotBlank
    @Min(5)
    private String username;
    @NotBlank
    private String password;
//    other info
}
