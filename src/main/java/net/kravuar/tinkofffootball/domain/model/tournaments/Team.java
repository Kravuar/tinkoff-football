package net.kravuar.tinkofffootball.domain.model.tournaments;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.kravuar.tinkofffootball.domain.model.user.User;

@Entity
@Getter
@Setter
@Table(name="teams")
@NoArgsConstructor
public class Team {
    public enum SecondPlayerStatus {
        INVITED,
        JOINED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User captain;

    @ManyToOne(fetch = FetchType.LAZY)
    private User secondPlayer;

    @Column(nullable = false)
    @Enumerated
    private SecondPlayerStatus secondPlayerStatus = SecondPlayerStatus.INVITED;

    public Team(User captain) {
        this.captain = captain;
    }
}
