package net.kravuar.tinkofffootball.application.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.props.JWTProps;
import net.kravuar.tinkofffootball.domain.model.dto.SignInFormDTO;
import net.kravuar.tinkofffootball.domain.model.dto.SignUpFormDTO;
import net.kravuar.tinkofffootball.domain.model.user.User;
import net.kravuar.tinkofffootball.domain.model.user.UserInfo;
import net.kravuar.tinkofffootball.domain.model.util.LoggedUser;
import net.kravuar.tinkofffootball.domain.security.JWTExtractor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final JWTProps jwtProps;
    private final JWTExtractor jwtExtractor;

    public LoggedUser signUp(SignUpFormDTO signUpForm) {
        var user = new User();
        user.setUsername(signUpForm.getUsername());
        user.setPassword(passwordEncoder.encode(signUpForm.getPassword()));

        user = userService.signUp(user);

        return getLoggedUser(new UserInfo(user.getId(), user.getUsername()));
    }

    public LoggedUser singIn(SignInFormDTO signInForm) {
        var user = userService.findOrElseThrow(signInForm.getUsername());
        if (!passwordEncoder.matches(signInForm.getPassword(), user.getPassword()))
            throw new BadCredentialsException("Неправильный пароль");

        return getLoggedUser(new UserInfo(user.getId(), signInForm.getUsername()));
    }

    public LoggedUser refresh(HttpServletRequest request) {
        var refreshToken = jwtExtractor.extract(request, jwtProps.getRefreshCookieName());
        var decodedJWT = jwtUtils.decode(refreshToken);

        return getLoggedUser(new UserInfo(decodedJWT.getClaim("id").asLong(), decodedJWT.getSubject()));
    }

    private LoggedUser getLoggedUser(UserInfo userInfo) {
        var accessToken = jwtUtils.sign(
                jwtUtils.getJWTBuilder(
                        userInfo.getUsername(),
                        Collections.emptyList(),
                        jwtProps.getAccessTokenExpiration()
                ).withClaim("id", userInfo.getId())
        );
        var refreshToken = jwtUtils.sign(
                jwtUtils.getJWTBuilder(
                        userInfo.getUsername(),
                        Collections.emptyList(),
                        jwtProps.getRefreshTokenExpiration()
                ).withClaim("id", userInfo.getId())
        );

        return new LoggedUser(accessToken, refreshToken, userInfo);
    }
}
