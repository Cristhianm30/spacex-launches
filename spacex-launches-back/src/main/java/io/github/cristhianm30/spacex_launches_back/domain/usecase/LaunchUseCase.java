package io.github.cristhianm30.spacex_launches_back.domain.usecase;

import io.github.cristhianm30.spacex_launches_back.domain.model.LaunchModel;
import io.github.cristhianm30.spacex_launches_back.domain.port.in.LaunchUseCasePort;
import io.github.cristhianm30.spacex_launches_back.domain.port.out.LaunchRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LaunchUseCase implements LaunchUseCasePort {

    private final LaunchRepositoryPort repositoryPort;

    @Override
    public Optional<LaunchModel> getLaunchById(String id) {
        return repositoryPort.findById(id);
    }

    @Override
    public List<LaunchModel> getAllLaunches() {
        return repositoryPort.findAll();
    }

    @Override
    public List<LaunchModel> getLaunchesByStatus(String status) {
        return repositoryPort.findByStatus(status);
    }

    @Override
    public List<LaunchModel> getLaunchesByRocket(String rocketId) {
        return repositoryPort.findByRocketId(rocketId);
    }

    @Override
    public List<LaunchModel> getSuccessfulLaunches() {
        return repositoryPort.findByStatus("success");
    }

    @Override
    public List<LaunchModel> getFailedLaunches() {
        return repositoryPort.findAll()
                .stream()
                .filter(launch -> !"success".equals(launch.getStatus()))
                .collect(Collectors.toList());
    }
}