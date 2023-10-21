package net.kravuar.tinkofffootball.application.props;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("web.jwt")
@Getter
public class JWTProps {
    private String tokenPrefix = "Bearer_";
    private long accessTokenExpiration = 300;
    private long refreshTokenExpiration = 43200;
    private String issuer = "jwt-auth";
    private String authoritiesClaimName = "authorities";
    private String accessCookieName = "access";
    private String accessCookiePath = "/";
    private String refreshCookieName = "refresh";
    private String refreshCookiePath = "/auth/refresh";
}
