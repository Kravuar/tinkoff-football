package net.kravuar.tinkofffootball.domain.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.services.JWTUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {
    private final JWTExtractor jwtExtractor;
    private final String jwtName;
    private final PrincipalExtractor principalExtractor;
    private final JWTUtils jwtUtils;
    private final String authoritiesClaimName;
    private final RequestMatcher ignoringMatcher;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            var decodedJWT = jwtUtils.decode(jwtExtractor.extract(request, jwtName));
            var principal = principalExtractor.extract(decodedJWT);
            var authorities = Arrays.stream(decodedJWT.getClaim(authoritiesClaimName).asArray(String.class))
                    .map(SimpleGrantedAuthority::new).toList();
            var token = new JWTAuthenticationToken(decodedJWT, principal, authorities);
            SecurityContextHolder.getContext().setAuthentication(token);
            filterChain.doFilter(request, response);
        } catch (JWTVerificationException exception) {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return ignoringMatcher.matches(request);
    }
}