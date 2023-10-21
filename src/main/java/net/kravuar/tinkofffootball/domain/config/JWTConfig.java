package net.kravuar.tinkofffootball.domain.config;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.kravuar.tinkofffootball.domain.model.user.UserInfo;
import net.kravuar.tinkofffootball.domain.security.JWTExtractor;
import net.kravuar.tinkofffootball.domain.security.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.WebUtils;

import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@RequiredArgsConstructor
public class JWTConfig {
    @Bean
    @SneakyThrows
    public Algorithm algorithm() {
        var generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        var keyPair = generator.generateKeyPair();
        var publicKey = (RSAPublicKey) keyPair.getPublic();
        var privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return Algorithm.RSA256(publicKey, privateKey);
    }

    @Bean
    public JWTExtractor jwtExtractor() {
        return (request, jwtName) -> {
            var cookie = WebUtils.getCookie(
                    request,
                    jwtName
            );
            return cookie != null ? cookie.getValue() : "";
        };
    }

    @Bean
    public PrincipalExtractor principalExtractor() {
        return decodedJWT -> new UserInfo(
                decodedJWT.getClaim("id").asLong(),
                decodedJWT.getSubject()
        );
    }
}
