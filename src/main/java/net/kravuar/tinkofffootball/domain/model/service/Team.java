package net.kravuar.tinkofffootball.domain.model.service;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.kravuar.tinkofffootball.domain.model.user.User;

@Entity
@Getter
@Setter
@Table(name="teams")
public class Team {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User captain;

    @ManyToOne(fetch = FetchType.LAZY)
    private User secondPlayer;
}
