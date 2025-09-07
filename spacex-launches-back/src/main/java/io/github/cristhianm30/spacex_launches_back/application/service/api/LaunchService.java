package io.github.cristhianm30.spacex_launches_back.application.service.api;

import io.github.cristhianm30.spacex_launches_back.application.dto.response.LaunchResponse;
import io.github.cristhianm30.spacex_launches_back.application.dto.response.LaunchSummaryResponse;

import java.util.List;
import java.util.Optional;

public interface LaunchService {
    Optional<LaunchResponse> getLaunchById(String id);
    List<LaunchSummaryResponse> getAllLaunches();
    List<LaunchSummaryResponse> getLaunchesByStatus(String status);
    List<LaunchSummaryResponse> getLaunchesByRocket(String rocketId);
    List<LaunchSummaryResponse> getSuccessfulLaunches();
    List<LaunchSummaryResponse> getFailedLaunches();
    long getTotalLaunchesCount();
    double getSuccessRate();
}