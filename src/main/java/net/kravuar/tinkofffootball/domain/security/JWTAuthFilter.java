package net.kravuar.tinkofffootball.domain.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class JWTAuthFilter extends AbstractAuthenticationProcessingFilter {
    private final JWTExtractor jwtExtractor;
    private final String jwtName;

    public JWTAuthFilter(AuthenticationManager authManager, JWTExtractor jwtExtractor, String jwtName) {
        super(new AntPathRequestMatcher("/**"), authManager);
        this.jwtExtractor = jwtExtractor;
        this.jwtName = jwtName;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        var token = new JWTAuthenticationToken(jwtExtractor.extract(request, jwtName));
        return getAuthenticationManager().authenticate(token);
    }
}