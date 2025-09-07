package io.github.cristhianm30.spacex_launches_back.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaunchResponse {
    private String launchId;
    private String missionName;
    private Integer flightNumber;
    private String launchDateUtc;
    private Boolean success;
    private String details;
    private String rocketId;
    private String launchpadId;
    private List<String> payloads;
    private String patchSmallLink;
    private String patchLargeLink;
    private String webcastLink;
    private String articleLink;
    private String wikipediaLink;
    private String status;
}
