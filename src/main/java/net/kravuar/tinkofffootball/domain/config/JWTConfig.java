package net.kravuar.tinkofffootball.domain.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import net.kravuar.tinkofffootball.domain.model.user.UserInfo;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

public class JWTConfig {

    @Bean
    public Function<DecodedJWT, Object> principalExtractor() {
        return decodedJWT -> new UserInfo(
                decodedJWT.getClaim("id").asLong(),
                decodedJWT.getSubject()
        );
    }
}
