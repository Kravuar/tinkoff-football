package net.kravuar.tinkofffootball.application.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("web")
@Data
public class WebProps {
    private List<String> allowedOrigins = new ArrayList<>();
    private List<String> unauthenticatedEndpoints = new ArrayList<>();
}
