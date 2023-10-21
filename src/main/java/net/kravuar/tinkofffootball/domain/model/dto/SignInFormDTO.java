package net.kravuar.tinkofffootball.domain.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignInFormDTO {
    @NotBlank
    @Size(min=5)
    private String username;
    @NotBlank
    private String password;
}
