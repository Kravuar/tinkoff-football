package net.kravuar.tinkofffootball.domain.security;

import jakarta.servlet.http.HttpServletRequest;

public interface JWTExtractor {

    String extract(HttpServletRequest request, String jwtName);
}
