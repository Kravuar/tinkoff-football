package net.kravuar.tinkofffootball.application.web;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.kravuar.jwtauth.components.CookieUtils;
import net.kravuar.tinkofffootball.application.services.AuthService;
import net.kravuar.tinkofffootball.domain.model.dto.SignInFormDTO;
import net.kravuar.tinkofffootball.domain.model.dto.SignUpFormDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final CookieUtils cookieUtils;

    @PostMapping("/signup")
    public void signup(SignUpFormDTO signUpForm) {
        authService.signup(signUpForm);
    }

    @PostMapping("/signin")
    public void signin(SignInFormDTO signInForm, HttpServletResponse response) {
        var jwts = authService.singin(signInForm);
        var cookies = cookieUtils.getJWTCookies(jwts.get(0), jwts.get(1));
        for (var cookie: cookies)
            response.addCookie(cookie);
    }
}
