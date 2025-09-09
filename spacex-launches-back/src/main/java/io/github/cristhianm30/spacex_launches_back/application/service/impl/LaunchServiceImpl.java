package io.github.cristhianm30.spacex_launches_back.application.service.impl;

import io.github.cristhianm30.spacex_launches_back.application.dto.response.LaunchResponse;
import io.github.cristhianm30.spacex_launches_back.application.dto.response.LaunchSummaryResponse;
import io.github.cristhianm30.spacex_launches_back.application.dto.response.StatsDataResponse;
import io.github.cristhianm30.spacex_launches_back.domain.util.constant.LaunchStatusConstants;
import io.github.cristhianm30.spacex_launches_back.application.mapper.LaunchMapperDto;
import io.github.cristhianm30.spacex_launches_back.application.service.api.LaunchService;
import io.github.cristhianm30.spacex_launches_back.domain.model.LaunchModel;
import io.github.cristhianm30.spacex_launches_back.domain.model.Page;
import io.github.cristhianm30.spacex_launches_back.domain.model.Pageable;
import io.github.cristhianm30.spacex_launches_back.domain.port.in.LaunchUseCasePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Page<LaunchSummaryResponse> getLaunches(String status, Pageable pageable) {
        Page<LaunchModel> domainPage = useCasePort.getLaunches(status, pageable);
        List<LaunchSummaryResponse> responseList = domainPage.getContent().stream().map(mapper::toSummaryResponse).collect(Collectors.toList());
        return new Page<>(responseList, domainPage.getNumber(), domainPage.getSize(), domainPage.getTotalElements());
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
    public StatsDataResponse getLaunchStats() {
        List<LaunchSummaryResponse> all = getAllLaunches();
        if (all.isEmpty()) {
            return new StatsDataResponse(0, 0.0, 0, 0, 0);
        }

        long successful = 0;
        long failed = 0;
        long upcoming = 0;

        for (LaunchSummaryResponse launch : all) {
            switch (launch.getStatus()) {
                case LaunchStatusConstants.SUCCESS:
                    successful++;
                    break;
                case LaunchStatusConstants.FAILED:
                    failed++;
                    break;
                case LaunchStatusConstants.UPCOMING:
                    upcoming++;
                    break;
            }
        }

        double successRate = (double) successful / all.size() * 100;

        return StatsDataResponse.builder()
                .totalLaunches(all.size())
                .successRate(successRate)
                .successfulLaunches((int) successful)
                .failedLaunches((int) failed)
                .upcomingLaunches((int) upcoming)
                .build();
    }
}
