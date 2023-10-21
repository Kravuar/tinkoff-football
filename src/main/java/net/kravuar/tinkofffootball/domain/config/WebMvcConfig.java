package net.kravuar.tinkofffootball.domain.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.kravuar.tinkofffootball.application.props.JWTProps;
import net.kravuar.tinkofffootball.application.props.WebProps;
import net.kravuar.tinkofffootball.application.services.JWTUtils;
import net.kravuar.tinkofffootball.domain.security.JWTAuthFilter;
import net.kravuar.tinkofffootball.domain.security.JWTAuthenticationProvider;
import net.kravuar.tinkofffootball.domain.security.JWTExtractor;
import net.kravuar.tinkofffootball.domain.security.PrincipalExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
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

    @Autowired
    public void authenticationManagerConfigure(AuthenticationManagerBuilder authenticationManagerBuilder, JWTUtils jwtUtils, PrincipalExtractor principalExtractor) {
        var jwtProvider = new JWTAuthenticationProvider(
                principalExtractor,
                jwtUtils,
                jwtProps.getAuthoritiesClaimName()
        );
        authenticationManagerBuilder.authenticationProvider(jwtProvider);
    }

    @SneakyThrows
    @Bean
    public JWTAuthFilter jwtFilter(AuthenticationConfiguration authenticationConfiguration, JWTExtractor jwtExtractor) {
        return new JWTAuthFilter(authenticationConfiguration.getAuthenticationManager(), jwtExtractor, jwtProps.getAccessCookieName());
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
        var unauthenticated = new OrRequestMatcher(
                webProps.getUnauthenticatedEndpoints().stream()
                        .map(AntPathRequestMatcher::new)
                        .map(RequestMatcher.class::cast)
                        .toList()
        );
        jwtAuthFilter.setRequiresAuthenticationRequestMatcher(new NegatedRequestMatcher(unauthenticated));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // The fck?
        httpSecurity.authorizeHttpRequests(config -> {
            config.requestMatchers(unauthenticated).permitAll();
            config.anyRequest().authenticated();
        });
//        httpSecurity.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
