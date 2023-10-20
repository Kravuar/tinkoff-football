package net.kravuar.tinkofffootball;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "net.kravuar.tinkofffootball.application.props")
public class TinkoffFootballApplication {

    public static void main(String[] args) {
        SpringApplication.run(TinkoffFootballApplication.class, args);
    }

}
