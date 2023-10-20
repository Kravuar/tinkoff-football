package net.kravuar.tinkofffootball.application.repo;

import net.kravuar.tinkofffootball.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
}
