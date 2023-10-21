package net.kravuar.tinkofffootball.application.repo;

import net.kravuar.tinkofffootball.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends PagingAndSortingRepository<User, Long>, JpaRepository<User, Long> {
    List<User> findAllByUsernameStartsWith(String prefix);
    Optional<User> findUserByUsername(String username);
}
