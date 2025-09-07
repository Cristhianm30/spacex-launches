package io.github.cristhianm30.spacex_launches_back.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SpaceX Launches API")
                        .version("1.0")
                        .description("Backend API to query SpaceX launches stored in DynamoDB"));
    }
}
