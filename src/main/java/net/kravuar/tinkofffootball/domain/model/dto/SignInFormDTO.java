package net.kravuar.tinkofffootball.domain.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInFormDTO {
    @NotBlank
    @Min(5)
    private String username;
    @NotBlank
    private String password;
}
