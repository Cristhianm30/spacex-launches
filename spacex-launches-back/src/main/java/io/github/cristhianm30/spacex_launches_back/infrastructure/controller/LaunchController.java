package io.github.cristhianm30.spacex_launches_back.infrastructure.controller;

import io.github.cristhianm30.spacex_launches_back.application.dto.response.LaunchResponse;
import io.github.cristhianm30.spacex_launches_back.application.dto.response.LaunchSummaryResponse;
import io.github.cristhianm30.spacex_launches_back.application.service.api.LaunchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        return ResponseEntity.ok(service.getSuccessfulLaunches());
    }
    
    @GetMapping("/failed")
    public ResponseEntity<List<LaunchSummaryResponse>> getFailedLaunches() {
        return ResponseEntity.ok(service.getFailedLaunches());
    }
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getLaunchStats() {
        return ResponseEntity.ok(Map.of(
            "totalLaunches", service.getTotalLaunchesCount(),
            "successRate", service.getSuccessRate(),
            "successfulLaunches", service.getSuccessfulLaunches().size(),
            "failedLaunches", service.getFailedLaunches().size()
        ));
    }
}