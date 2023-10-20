package net.kravuar.tinkofffootball.application.services;

import lombok.RequiredArgsConstructor;
import net.kravuar.jwtauth.components.JWTUtils;
import net.kravuar.jwtauth.components.props.JWTProps;
import net.kravuar.tinkofffootball.domain.model.dto.SignInFormDTO;
import net.kravuar.tinkofffootball.domain.model.dto.SignUpFormDTO;
import net.kravuar.tinkofffootball.domain.model.user.User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JWTUtils jwtUtils;
    private final JWTProps jwtProps;

    public void signup(SignUpFormDTO signUpForm) {
//      TODO: simple signup
    }

    public List<String> singin(SignInFormDTO signInForm) {
        var user = (User) null;
//        validate credentials

        var accessToken = jwtUtils.sign(
                jwtUtils.getJWTBuilder(
                    signInForm.getUsername(),
                    Collections.emptyList(),
                    jwtProps.getAccessTokenExpiration()
                ).withClaim("id", user.getId())
        );
        var refreshToken = jwtUtils.sign(
                jwtUtils.getJWTBuilder(
                        signInForm.getUsername(),
                        Collections.emptyList(),
                        jwtProps.getRefreshTokenExpiration()
                ).withClaim("id", user.getId())
        );
        return List.of(accessToken, refreshToken);
    }
}
