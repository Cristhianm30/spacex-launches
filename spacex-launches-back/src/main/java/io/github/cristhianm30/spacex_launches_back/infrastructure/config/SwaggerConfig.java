package io.github.cristhianm30.spacex_launches_back.infrastructure.config;

import io.github.cristhianm30.spacex_launches_back.domain.util.constant.ApiConstants;
import io.github.cristhianm30.spacex_launches_back.domain.util.constant.CorsConstants;
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
                title = ApiConstants.API_TITLE,
                version = ApiConstants.API_VERSION,
                description = ApiConstants.API_DESCRIPTION,
                contact = @Contact(name = ApiConstants.CONTACT_NAME, email = ApiConstants.CONTACT_EMAIL),
                license = @License(name = ApiConstants.LICENSE_NAME)
        ),
        servers = {
                @Server(url = ApiConstants.BASE_SERVER_URL, description = ApiConstants.BASE_SERVER_DESCRIPTION),
                @Server(url = ApiConstants.PRODUCTION_SERVER_URL, description = ApiConstants.PRODUCTION_SERVER_DESCRIPTION)
        }
)
public class SwaggerConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping(CorsConstants.DOCS_MAPPING)
                        .allowedOrigins(CorsConstants.ALLOWED_ORIGINS)
                        .allowedMethods(CorsConstants.ALLOWED_METHODS)
                        .allowedHeaders(CorsConstants.ALLOWED_HEADERS);
                registry.addMapping(CorsConstants.SWAGGER_UI_MAPPING)
                        .allowedOrigins(CorsConstants.ALLOWED_ORIGINS)
                        .allowedMethods(CorsConstants.ALLOWED_METHODS)
                        .allowedHeaders(CorsConstants.ALLOWED_HEADERS);
            }
        };
    }
}
