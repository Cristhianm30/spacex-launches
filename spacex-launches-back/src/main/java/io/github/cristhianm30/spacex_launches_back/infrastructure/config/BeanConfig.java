package io.github.cristhianm30.spacex_launches_back.infrastructure.config;

import io.github.cristhianm30.spacex_launches_back.domain.port.out.LaunchRepositoryPort;
import io.github.cristhianm30.spacex_launches_back.domain.usecase.LaunchUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public LaunchUseCase spaceXLaunchUseCase(LaunchRepositoryPort repositoryPort) {
        return new LaunchUseCase(repositoryPort);
    }
}
