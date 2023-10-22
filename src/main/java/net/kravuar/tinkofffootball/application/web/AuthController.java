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
import net.kravuar.tinkofffootball.domain.model.dto.SignInFormDTO;
import net.kravuar.tinkofffootball.domain.model.dto.SignUpFormDTO;
import net.kravuar.tinkofffootball.domain.model.dto.UserInfoDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final CookieService cookieService;


    @Operation(summary = "Регистрация", description = "Создать аккаунт.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная регистрация, jwt cookie получены."),
            @ApiResponse(responseCode = "400", description = "Неподходящие данные"),
    })
    @PostMapping("/signUp")
    public UserInfoDTO signUp(@RequestBody @Valid SignUpFormDTO signUpForm, HttpServletResponse response) {
        var loggedUser = authService.signUp(signUpForm);
        cookieService.getJWTCookies(loggedUser.getAccessToken(), loggedUser.getRefreshToken()).forEach(response::addCookie);
        return new UserInfoDTO(loggedUser.getUserInfo());
    }


    @Operation(summary = "Обновление JWT", description = "Обновить пару JWT токенов.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное обновление, jwt cookie получены."),
            @ApiResponse(responseCode = "403", description = "Отсутствует refresh token"),
    })
    @GetMapping("/refresh")
    public void refresh(HttpServletRequest request, HttpServletResponse response) {
        var loggedUser = authService.refresh(request);
        cookieService.getJWTCookies(loggedUser.getAccessToken(), loggedUser.getRefreshToken()).forEach(response::addCookie);
    }

    @Operation(summary = "Авторизация", description = "Войти в аккаунт.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная авторизация, jwt cookie получены."),
            @ApiResponse(responseCode = "403", description = "Неправильный пароль"),
    })
    @PostMapping("/signIn")
    public UserInfoDTO signIn(@RequestBody @Valid SignInFormDTO signInForm, HttpServletResponse response) {
        var loggedUser = authService.singIn(signInForm);
        cookieService.getJWTCookies(loggedUser.getAccessToken(), loggedUser.getRefreshToken()).forEach(response::addCookie);
        return new UserInfoDTO(loggedUser.getUserInfo());
    }

    @Operation(summary = "Выход из аккаунта", description = "Выйти из аккаунта.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JWT cookie удалены."),
    })
    @GetMapping("/logout")
    public void logout(HttpServletResponse response) {
        cookieService.getDeleteCookies().forEach(response::addCookie);
    }
}
