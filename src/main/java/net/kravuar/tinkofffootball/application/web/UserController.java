package net.kravuar.tinkofffootball.application.web;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.services.UserService;
import net.kravuar.tinkofffootball.domain.model.dto.DetailedUserInfoDTO;
import net.kravuar.tinkofffootball.domain.model.dto.UserInfoDTO;
import net.kravuar.tinkofffootball.domain.model.user.UserInfo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/info")
    public UserInfoDTO getUserInfo(@AuthenticationPrincipal UserInfo userInfo) {
        return userService.getUserInfo(userInfo.getId());
    }

    @GetMapping("/info/detailed")
    public DetailedUserInfoDTO getDetailedUserInfo(@AuthenticationPrincipal UserInfo userInfo) {
        return userService.getDetailedUserInfo(userInfo.getId());
    }

    @GetMapping("/findByUsername/{username}")
    public Collection<UserInfoDTO> findByUsername(@PathVariable @NotBlank String username) {
        return userService.findByUsername(username);
    }
}
