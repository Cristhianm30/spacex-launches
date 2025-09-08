package io.github.cristhianm30.spacex_launches_back.infrastructure.controller;

import io.github.cristhianm30.spacex_launches_back.application.dto.response.LaunchResponse;
import io.github.cristhianm30.spacex_launches_back.application.dto.response.LaunchSummaryResponse;
import io.github.cristhianm30.spacex_launches_back.application.dto.response.StatsDataResponse;
import io.github.cristhianm30.spacex_launches_back.application.service.api.LaunchService;
import io.github.cristhianm30.spacex_launches_back.domain.model.Page;
import io.github.cristhianm30.spacex_launches_back.domain.model.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/launches")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class LaunchController {

    private final LaunchService service;

    @GetMapping("/{id}")
    public ResponseEntity<LaunchResponse> getLaunchById(@PathVariable String id) {
        return service.getLaunchById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<LaunchSummaryResponse>> getAllLaunches() {
        return ResponseEntity.ok(service.getAllLaunches());
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<LaunchSummaryResponse>> getLaunches(
            @RequestParam(required = false) String status,
            Pageable pageable) {
        return ResponseEntity.ok(service.getLaunches(status, pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<LaunchSummaryResponse>> getLaunchesByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.getLaunchesByStatus(status));
    }

    @GetMapping("/rocket/{rocketId}")
    public ResponseEntity<List<LaunchSummaryResponse>> getLaunchesByRocket(@PathVariable String rocketId) {
        return ResponseEntity.ok(service.getLaunchesByRocket(rocketId));
    }

    @GetMapping("/successful")
    public ResponseEntity<List<LaunchSummaryResponse>> getSuccessfulLaunches() {
        return ResponseEntity.ok(service.getLaunchesByStatus("success"));
    }

    @GetMapping("/failed")
    public ResponseEntity<List<LaunchSummaryResponse>> getFailedLaunches() {
        return ResponseEntity.ok(service.getLaunchesByStatus("failed"));
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsDataResponse> getLaunchStats() {
        return ResponseEntity.ok(service.getLaunchStats());
    }
}
