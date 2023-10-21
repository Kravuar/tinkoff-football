package net.kravuar.tinkofffootball.application.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.services.AuthService;
import net.kravuar.tinkofffootball.application.services.CookieService;
import net.kravuar.tinkofffootball.domain.model.dto.SignInFormDTO;
import net.kravuar.tinkofffootball.domain.model.dto.SignUpFormDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final CookieService cookieService;

    @PostMapping("/signUp")
    public void signUp(@Valid SignUpFormDTO signUpForm, HttpServletResponse response) {
        System.out.println("signup");
        var jwts = authService.signUp(signUpForm);
        System.out.println("done");
        System.out.println(jwts.get(0));
        cookieService.getJWTCookies(jwts.get(0), jwts.get(1)).forEach(response::addCookie);
    }

    @GetMapping("/refresh")
    public void refresh(HttpServletRequest request, HttpServletResponse response) {
        var jwts = authService.refresh(request);
        cookieService.getJWTCookies(jwts.get(0), jwts.get(1)).forEach(response::addCookie);
    }

    @PostMapping("/signIn")
    public void signIn(@Valid SignInFormDTO signInForm, HttpServletResponse response) {
        var jwts = authService.singIn(signInForm);
        cookieService.getJWTCookies(jwts.get(0), jwts.get(1)).forEach(response::addCookie);
    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response) {
        cookieService.getDeleteCookies().forEach(response::addCookie);
    }
}
