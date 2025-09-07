package io.github.cristhianm30.spacex_launches_back.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaunchSummaryResponse {
    private String launchId;
    private String missionName;
    private Integer flightNumber;
    private String launchDateUtc;
    private String status;
    private String rocketId;
}
