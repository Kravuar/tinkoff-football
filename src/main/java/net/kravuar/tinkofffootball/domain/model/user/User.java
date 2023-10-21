package net.kravuar.tinkofffootball.domain.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="users")
public class User {
    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank
    @Min(5)
    private String username;

    @Column(nullable = false)
    @NotBlank
    private String password;

    //    TODO: profile pic
}
