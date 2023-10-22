package net.kravuar.tinkofffootball.domain.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.kravuar.tinkofffootball.application.props.JWTProps;
import net.kravuar.tinkofffootball.application.props.WebProps;
import net.kravuar.tinkofffootball.application.services.JWTUtils;
import net.kravuar.tinkofffootball.domain.security.JWTAuthFilter;
import net.kravuar.tinkofffootball.domain.security.JWTExtractor;
import net.kravuar.tinkofffootball.domain.security.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig {
    private final WebProps webProps;
    private final JWTProps jwtProps;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(webProps.getAllowedOrigins());
        configuration.setAllowedHeaders(List.of("Content-Type"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return new CorsFilter(source);
    }

    @SneakyThrows
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, JWTAuthFilter jwtAuthFilter) {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // The fck?
        httpSecurity.authorizeHttpRequests(config -> {
            config.requestMatchers(new OrRequestMatcher(
                webProps.getUnauthenticatedEndpoints().stream()
                        .map(AntPathRequestMatcher::new)
                        .map(RequestMatcher.class::cast)
                        .toList()
            )).permitAll();
            config.anyRequest().authenticated();
        });
        httpSecurity.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public JWTAuthFilter jwtAuthFilter(JWTExtractor jwtExtractor, PrincipalExtractor principalExtractor, JWTUtils jwtUtils) {
        return new JWTAuthFilter(jwtExtractor, jwtProps.getAccessCookieName(), principalExtractor, jwtUtils, jwtProps.getAuthoritiesClaimName(), new OrRequestMatcher(
                webProps.getUnauthenticatedEndpoints().stream()
                        .map(AntPathRequestMatcher::new)
                        .map(RequestMatcher.class::cast)
                        .toList()
        ));
    }
}
