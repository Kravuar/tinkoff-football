package net.kravuar.tinkofffootball.application.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.props.JWTProps;
import net.kravuar.tinkofffootball.domain.model.dto.SignInFormDTO;
import net.kravuar.tinkofffootball.domain.model.dto.SignUpFormDTO;
import net.kravuar.tinkofffootball.domain.model.user.User;
import net.kravuar.tinkofffootball.domain.model.user.UserInfo;
import net.kravuar.tinkofffootball.domain.security.JWTExtractor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final JWTProps jwtProps;
    private final JWTExtractor jwtExtractor;

    public List<String> signUp(SignUpFormDTO signUpForm) {
        var user = new User();
        user.setUsername(signUpForm.getUsername());
        user.setPassword(passwordEncoder.encode(signUpForm.getPassword()));

        user = userService.signUp(user);

        return getTokens(new UserInfo(user.getId(), user.getUsername()));
    }

    public List<String> singIn(SignInFormDTO signInForm) {
        var user = userService.findOrElseThrow(signInForm.getUsername());
        if (!passwordEncoder.matches(signInForm.getPassword(), user.getPassword()))
            throw new BadCredentialsException("invalid-password");

        return getTokens(new UserInfo(user.getId(), signInForm.getUsername()));
    }

    public List<String> refresh(HttpServletRequest request) {
        var refreshToken = jwtExtractor.extract(request, jwtProps.getRefreshCookieName());
        var decodedJWT = jwtUtils.decode(refreshToken);

        return getTokens(new UserInfo(decodedJWT.getClaim("id").asLong(), decodedJWT.getSubject()));
    }

    private List<String> getTokens(UserInfo userInfo) {
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

        return List.of(accessToken, refreshToken);
    }
}
