package io.github.cristhianm30.spacex_launches_back.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatsDataResponse {

    private long totalLaunches;
    private double successRate;
    private int successfulLaunches;
    private int failedLaunches;
    private int upcomingLaunches;
}
