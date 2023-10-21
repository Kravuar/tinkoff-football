package net.kravuar.tinkofffootball.application.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.services.AuthService;
import net.kravuar.tinkofffootball.application.services.CookieService;
import net.kravuar.tinkofffootball.application.services.UserService;
import net.kravuar.tinkofffootball.domain.model.dto.SignInFormDTO;
import net.kravuar.tinkofffootball.domain.model.dto.SignUpFormDTO;
import net.kravuar.tinkofffootball.domain.model.dto.UserInfoDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final CookieService cookieService;


    @Operation(summary = "Регистрация")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная регистрация, jwt cookie получены."),
    })
    @PostMapping("/signUp")
    public UserInfoDTO signUp(@RequestBody @Valid SignUpFormDTO signUpForm, HttpServletResponse response) {
        var loggedUser = authService.signUp(signUpForm);
        cookieService.getJWTCookies(loggedUser.getAccessToken(), loggedUser.getRefreshToken()).forEach(response::addCookie);
        return new UserInfoDTO(loggedUser.getUserInfo());
    }


    /**
     * Обновление jwt (возвращает jwt cookie set).
     *
     */
    @GetMapping("/refresh")
    public void refresh(HttpServletRequest request, HttpServletResponse response) {
        var loggedUser = authService.refresh(request);
        cookieService.getJWTCookies(loggedUser.getAccessToken(), loggedUser.getRefreshToken()).forEach(response::addCookie);
    }

    /**
     * Авторизация (возвращает jwt cookie set).
     *
     * @param signInForm форма авторизации.
     */
    @PostMapping("/signIn")
    public UserInfoDTO signIn(@RequestBody @Valid SignInFormDTO signInForm, HttpServletResponse response) {
        var loggedUser = authService.singIn(signInForm);
        cookieService.getJWTCookies(loggedUser.getAccessToken(), loggedUser.getRefreshToken()).forEach(response::addCookie);
        return new UserInfoDTO(loggedUser.getUserInfo());
    }

    /**
     * Выход из аккаунта (удаляет jwt cookie set).
     *
     */
    @GetMapping("/logout")
    public void logout(HttpServletResponse response) {
        cookieService.getDeleteCookies().forEach(response::addCookie);
    }
}
