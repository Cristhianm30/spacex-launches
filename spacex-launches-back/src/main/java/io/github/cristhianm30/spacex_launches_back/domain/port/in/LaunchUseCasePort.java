package io.github.cristhianm30.spacex_launches_back.domain.port.in;

import io.github.cristhianm30.spacex_launches_back.domain.model.LaunchModel;

import java.util.List;
import java.util.Optional;

public interface LaunchUseCasePort {
    Optional<LaunchModel> getLaunchById(String id);
    List<LaunchModel> getAllLaunches();
    List<LaunchModel> getLaunchesByStatus(String status);
    List<LaunchModel> getLaunchesByRocket(String rocketId);
    List<LaunchModel> getSuccessfulLaunches();
    List<LaunchModel> getFailedLaunches();
}