package net.kravuar.tinkofffootball.application.services;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.props.JWTProps;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CookieService {
    private final JWTProps jwtProps;

    public List<Cookie> getJWTCookies(String accessToken, String refreshToken) {
        var refreshCookie = new Cookie(jwtProps.getAccessCookieName(), jwtProps.getTokenPrefix() + accessToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge((int) jwtProps.getRefreshTokenExpiration());
        refreshCookie.setPath(jwtProps.getAccessCookiePath());

        var accessCookie = new Cookie(jwtProps.getRefreshCookieName(), jwtProps.getTokenPrefix() + refreshToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setMaxAge((int) jwtProps.getAccessTokenExpiration());
        accessCookie.setPath(jwtProps.getRefreshCookiePath());

        return List.of(accessCookie, refreshCookie);
    }

    public List<Cookie> getDeleteCookies() {
        var refreshCookie = new Cookie(jwtProps.getAccessCookieName(), "deleted");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge(0);
        refreshCookie.setPath(jwtProps.getAccessCookiePath());

        var accessCookie = new Cookie(jwtProps.getRefreshCookieName(), "deleted");
        accessCookie.setHttpOnly(true);
        accessCookie.setMaxAge(0);
        accessCookie.setPath(jwtProps.getRefreshCookiePath());
        return List.of(accessCookie, refreshCookie);
    }
}