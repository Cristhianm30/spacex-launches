package io.github.cristhianm30.spacex_launches_back.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "SpaceX Launches API",
                version = "v1",
                description = "API para consultar lanzamientos SpaceX almacenados en DynamoDB",
                contact = @Contact(name = "Cristhian Moreno", email = ""),
                license = @License(name = "MIT")
        ),
        servers = {
                @Server(url = "/api", description = "Base path detrás de API Gateway o reverse proxy"),
                @Server(url = "https://lbs33m5sf6.execute-api.us-east-1.amazonaws.com/prod", description = "Production")
        }
)
public class SwaggerConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/docs/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
                registry.addMapping("/swagger/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}
