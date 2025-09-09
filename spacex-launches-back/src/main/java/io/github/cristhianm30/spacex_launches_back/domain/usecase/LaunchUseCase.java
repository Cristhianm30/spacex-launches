package io.github.cristhianm30.spacex_launches_back.domain.usecase;

import io.github.cristhianm30.spacex_launches_back.domain.model.LaunchModel;
import io.github.cristhianm30.spacex_launches_back.domain.model.Page;
import io.github.cristhianm30.spacex_launches_back.domain.model.Pageable;
import io.github.cristhianm30.spacex_launches_back.domain.port.in.LaunchUseCasePort;
import io.github.cristhianm30.spacex_launches_back.domain.port.out.LaunchRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

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
    public Page<LaunchModel> getLaunches(String status, Pageable pageable) {
        return repositoryPort.findAll(status, pageable);
    }

    @Override
    public List<LaunchModel> getLaunchesByStatus(String status) {
        return repositoryPort.findByStatus(status);
    }

    @Override
    public List<LaunchModel> getLaunchesByRocket(String rocketId) {
        return repositoryPort.findByRocketId(rocketId);
    }
}
