package io.github.cristhianm30.spacex_launches_back.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

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
                @Server(url = "https://fwit88xlf0.execute-api.us-east-1.amazonaws.com/api", description = "Production")
        },
        security = {@SecurityRequirement(name = "bearerAuth")}
)
public class SwaggerConfig {
}
