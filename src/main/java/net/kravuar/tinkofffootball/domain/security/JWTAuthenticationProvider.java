package net.kravuar.tinkofffootball.domain.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.services.JWTUtils;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;

@RequiredArgsConstructor
public class JWTAuthenticationProvider implements AuthenticationProvider {
    private final PrincipalExtractor principalExtractor;
    private final JWTUtils jwtUtils;
    private final String authoritiesClaimName;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            JWTAuthenticationToken bearer = (JWTAuthenticationToken) authentication;
            var decodedJWT = jwtUtils.decode(bearer.getJwt());
            var principal = principalExtractor.extract(decodedJWT);
            var authorities = Arrays.stream(decodedJWT.getClaim(authoritiesClaimName).asArray(String.class))
                    .map(SimpleGrantedAuthority::new).toList();
            return new JWTAuthenticationToken(decodedJWT, principal, authorities);
        } catch (JWTVerificationException exception) {
            throw new AuthenticationCredentialsNotFoundException(exception.getMessage(), exception);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JWTAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
