package io.github.cristhianm30.spacex_launches_back.application.service.api;

import io.github.cristhianm30.spacex_launches_back.application.dto.response.LaunchResponse;
import io.github.cristhianm30.spacex_launches_back.application.dto.response.LaunchSummaryResponse;
import io.github.cristhianm30.spacex_launches_back.application.dto.response.StatsDataResponse;
import io.github.cristhianm30.spacex_launches_back.domain.model.Page;
import io.github.cristhianm30.spacex_launches_back.domain.model.Pageable;


import java.util.List;
import java.util.Optional;

public interface LaunchService {
    Optional<LaunchResponse> getLaunchById(String id);
    List<LaunchSummaryResponse> getAllLaunches();
    Page<LaunchSummaryResponse> getLaunches(String status, Pageable pageable);
    List<LaunchSummaryResponse> getLaunchesByStatus(String status);
    List<LaunchSummaryResponse> getLaunchesByRocket(String rocketId);
    StatsDataResponse getLaunchStats();
}
