package net.kravuar.tinkofffootball.domain.model.tournaments;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="tournaments")
public class Tournament {
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "tournament")
    private Set<Match> matches = new HashSet<>();
}
