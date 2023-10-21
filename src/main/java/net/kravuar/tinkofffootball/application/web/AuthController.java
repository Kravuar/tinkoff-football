package net.kravuar.tinkofffootball.application.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.services.AuthService;
import net.kravuar.tinkofffootball.application.services.CookieService;
import net.kravuar.tinkofffootball.domain.model.dto.SignInFormDTO;
import net.kravuar.tinkofffootball.domain.model.dto.SignUpFormDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final CookieService cookieService;

    @PostMapping("/signUp")
    public void signUp(@RequestBody @Valid SignUpFormDTO signUpForm, HttpServletResponse response) {
        var jwts = authService.signUp(signUpForm);
        cookieService.getJWTCookies(jwts.get(0), jwts.get(1)).forEach(response::addCookie);
    }

    @GetMapping("/refresh")
    public void refresh(HttpServletRequest request, HttpServletResponse response) {
        var jwts = authService.refresh(request);
        cookieService.getJWTCookies(jwts.get(0), jwts.get(1)).forEach(response::addCookie);
    }

    @PostMapping("/signIn")
    public void signIn(@RequestBody @Valid SignInFormDTO signInForm, HttpServletResponse response) {
        var jwts = authService.singIn(signInForm);
        cookieService.getJWTCookies(jwts.get(0), jwts.get(1)).forEach(response::addCookie);
    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response) {
        cookieService.getDeleteCookies().forEach(response::addCookie);
    }
}
