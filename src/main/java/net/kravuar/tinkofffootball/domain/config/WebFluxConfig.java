package net.kravuar.tinkofffootball.domain.config;

import lombok.RequiredArgsConstructor;
import net.kravuar.tinkofffootball.application.props.WebProps;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebFluxConfig {
    private final WebProps webProps;

    @Bean
    public CorsWebFilter corsWebFilter() {
        var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(webProps.getAllowedOrigins());
        configuration.setAllowedHeaders(List.of("Content-Type"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return new CorsWebFilter(source);
    }
}
