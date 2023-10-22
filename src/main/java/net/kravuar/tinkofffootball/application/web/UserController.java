package net.kravuar.tinkofffootball.application.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.services.UserService;
import net.kravuar.tinkofffootball.domain.model.dto.DetailedUserInfoDTO;
import net.kravuar.tinkofffootball.domain.model.dto.UserInfoDTO;
import net.kravuar.tinkofffootball.domain.model.user.UserInfo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @Operation(summary = "Информация о пользователе.", description = "Возвращает информацию о текущем пользователе.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация получена."),
            @ApiResponse(responseCode = "403", description = "Не авторизован.")
    })
    @GetMapping("/info")
    public UserInfoDTO getUserInfo(@AuthenticationPrincipal UserInfo userInfo) {
        return userService.getUserInfo(userInfo.getId());
    }

    @Operation(summary = "Подробная информация о пользователе.", description = "Возвращает подробную информацию о текущем пользователе.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация получена."),
            @ApiResponse(responseCode = "403", description = "Не авторизован.")
    })
    @GetMapping("/info/detailed")
    public DetailedUserInfoDTO getDetailedUserInfo(@AuthenticationPrincipal UserInfo userInfo) {
        return userService.getDetailedUserInfo(userInfo.getId());
    }

    @Operation(summary = "Найти пользователей.", description = "Возвращает пользователей по префиксу имени.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователи получены."),
    })
    @GetMapping("/findByUsername/{username}")
    public Collection<UserInfoDTO> findByUsername(@PathVariable @NotBlank String username) {
        return userService.findByUsername(username);
    }
}
