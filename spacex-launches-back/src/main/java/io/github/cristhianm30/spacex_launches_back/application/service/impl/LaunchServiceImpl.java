package io.github.cristhianm30.spacex_launches_back.application.service.impl;

import io.github.cristhianm30.spacex_launches_back.application.dto.response.LaunchResponse;
import io.github.cristhianm30.spacex_launches_back.application.dto.response.LaunchSummaryResponse;
import io.github.cristhianm30.spacex_launches_back.application.mapper.LaunchMapperDto;
import io.github.cristhianm30.spacex_launches_back.application.service.api.LaunchService;
import io.github.cristhianm30.spacex_launches_back.domain.port.in.LaunchUseCasePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LaunchServiceImpl implements LaunchService {
    
    private final LaunchUseCasePort useCasePort;
    private final LaunchMapperDto mapper;
    
    @Override
    public Optional<LaunchResponse> getLaunchById(String id) {
        return useCasePort.getLaunchById(id)
                .map(mapper::toResponse);
    }
    
    @Override
    public List<LaunchSummaryResponse> getAllLaunches() {
        return mapper.toSummaryResponseList(useCasePort.getAllLaunches());
    }
    
    @Override
    public List<LaunchSummaryResponse> getLaunchesByStatus(String status) {
        return mapper.toSummaryResponseList(useCasePort.getLaunchesByStatus(status));
    }
    
    @Override
    public List<LaunchSummaryResponse> getLaunchesByRocket(String rocketId) {
        return mapper.toSummaryResponseList(useCasePort.getLaunchesByRocket(rocketId));
    }
    
    @Override
    public List<LaunchSummaryResponse> getSuccessfulLaunches() {
        return mapper.toSummaryResponseList(useCasePort.getSuccessfulLaunches());
    }
    
    @Override
    public List<LaunchSummaryResponse> getFailedLaunches() {
        return mapper.toSummaryResponseList(useCasePort.getFailedLaunches());
    }
    
    @Override
    public long getTotalLaunchesCount() {
        return useCasePort.getAllLaunches().size();
    }
    
    @Override
    public double getSuccessRate() {
        List<LaunchSummaryResponse> all = getAllLaunches();
        if (all.isEmpty()) return 0.0;
        
        long successful = all.stream()
                .mapToLong(launch -> "success".equals(launch.getStatus()) ? 1 : 0)
                .sum();
        
        return (double) successful / all.size() * 100;
    }
}
