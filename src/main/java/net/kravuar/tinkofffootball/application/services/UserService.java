package net.kravuar.tinkofffootball.application.services;

import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.repo.UserRepo;
import net.kravuar.tinkofffootball.domain.model.dto.DetailedUserInfoDTO;
import net.kravuar.tinkofffootball.domain.model.dto.UserInfoDTO;
import net.kravuar.tinkofffootball.domain.model.exceptions.ResourceNotFoundException;
import net.kravuar.tinkofffootball.domain.model.user.User;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public User findOrElseThrow(Long id) {
        return userRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("user", "id", id)
        );
    }

    public UserInfoDTO getUserInfo(Long id) {
        return new UserInfoDTO(findOrElseThrow(id));
    }

    public DetailedUserInfoDTO getDetailedUserInfo(Long id) {
        return new DetailedUserInfoDTO(findOrElseThrow(id));
    }

    public Collection<UserInfoDTO> findByUsername(String username) {
        return userRepo.findAllByUsernameStartsWith(username)
                .stream().map(UserInfoDTO::new).toList();
    }
}
