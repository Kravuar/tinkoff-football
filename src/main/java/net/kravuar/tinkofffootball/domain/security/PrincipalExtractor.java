package net.kravuar.tinkofffootball.domain.security;

import com.auth0.jwt.interfaces.DecodedJWT;

public interface PrincipalExtractor {

    Object extract(DecodedJWT jwt);
}
