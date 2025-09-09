package io.github.cristhianm30.spacex_launches_back.application.service.impl;

import io.github.cristhianm30.spacex_launches_back.application.dto.response.LaunchResponse;
import io.github.cristhianm30.spacex_launches_back.application.dto.response.LaunchSummaryResponse;
import io.github.cristhianm30.spacex_launches_back.application.dto.response.StatsDataResponse;
import io.github.cristhianm30.spacex_launches_back.application.mapper.LaunchMapperDto;
import io.github.cristhianm30.spacex_launches_back.domain.model.LaunchModel;
import io.github.cristhianm30.spacex_launches_back.domain.port.in.LaunchUseCasePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LaunchServiceImplTest {

    @Mock
    private LaunchUseCasePort useCasePort;

    @Mock
    private LaunchMapperDto mapper;

    @InjectMocks
    private LaunchServiceImpl launchService;

    private LaunchModel launchModel;
    private LaunchResponse launchResponse;
    private LaunchSummaryResponse launchSummaryResponse;

    @BeforeEach
    void setUp() {
        launchModel = LaunchModel.builder()
                .launchId("1")
                .missionName("Test Mission")
                .flightNumber(1)
                .status("success")
                .success(true)
                .rocketId("rocket1")
                .build();

        launchResponse = LaunchResponse.builder()
                .launchId("1")
                .missionName("Test Mission")
                .flightNumber(1)
                .status("success")
                .success(true)
                .rocketId("rocket1")
                .build();

        launchSummaryResponse = LaunchSummaryResponse.builder()
                .launchId("1")
                .missionName("Test Mission")
                .flightNumber(1)
                .status("success")
                .rocketId("rocket1")
                .build();
    }

    @Test
    void getLaunchById_WhenLaunchExists_ShouldReturnMappedResponse() {
        // Given
        String launchId = "1";
        when(useCasePort.getLaunchById(launchId)).thenReturn(Optional.of(launchModel));
        when(mapper.toResponse(launchModel)).thenReturn(launchResponse);

        // When
        Optional<LaunchResponse> result = launchService.getLaunchById(launchId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(launchResponse, result.get());
        verify(useCasePort).getLaunchById(launchId);
        verify(mapper).toResponse(launchModel);
    }

    @Test
    void getLaunchById_WhenLaunchNotExists_ShouldReturnEmpty() {
        // Given
        String launchId = "nonexistent";
        when(useCasePort.getLaunchById(launchId)).thenReturn(Optional.empty());

        // When
        Optional<LaunchResponse> result = launchService.getLaunchById(launchId);

        // Then
        assertFalse(result.isPresent());
        verify(useCasePort).getLaunchById(launchId);
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void getAllLaunches_ShouldReturnMappedSummaryList() {
        // Given
        List<LaunchModel> launches = Arrays.asList(launchModel);
        List<LaunchSummaryResponse> expectedResponses = Arrays.asList(launchSummaryResponse);

        when(useCasePort.getAllLaunches()).thenReturn(launches);
        when(mapper.toSummaryResponseList(launches)).thenReturn(expectedResponses);

        // When
        List<LaunchSummaryResponse> result = launchService.getAllLaunches();

        // Then
        assertEquals(expectedResponses, result);
        verify(useCasePort).getAllLaunches();
        verify(mapper).toSummaryResponseList(launches);
    }


    @Test
    void getLaunchesByStatus_ShouldReturnFilteredMappedList() {
        // Given
        String status = "success";
        List<LaunchModel> launches = Arrays.asList(launchModel);
        List<LaunchSummaryResponse> expectedResponses = Arrays.asList(launchSummaryResponse);

        when(useCasePort.getLaunchesByStatus(status)).thenReturn(launches);
        when(mapper.toSummaryResponseList(launches)).thenReturn(expectedResponses);

        // When
        List<LaunchSummaryResponse> result = launchService.getLaunchesByStatus(status);

        // Then
        assertEquals(expectedResponses, result);
        verify(useCasePort).getLaunchesByStatus(status);
        verify(mapper).toSummaryResponseList(launches);
    }

    @Test
    void getLaunchesByRocket_ShouldReturnFilteredMappedList() {
        // Given
        String rocketId = "rocket1";
        List<LaunchModel> launches = Arrays.asList(launchModel);
        List<LaunchSummaryResponse> expectedResponses = Arrays.asList(launchSummaryResponse);

        when(useCasePort.getLaunchesByRocket(rocketId)).thenReturn(launches);
        when(mapper.toSummaryResponseList(launches)).thenReturn(expectedResponses);

        // When
        List<LaunchSummaryResponse> result = launchService.getLaunchesByRocket(rocketId);

        // Then
        assertEquals(expectedResponses, result);
        verify(useCasePort).getLaunchesByRocket(rocketId);
        verify(mapper).toSummaryResponseList(launches);
    }

    @Test
    void getLaunchStats_ShouldReturnCorrectStats() {
        // Given
        LaunchSummaryResponse successfulLaunch = LaunchSummaryResponse.builder().status("success").build();
        LaunchSummaryResponse failedLaunch = LaunchSummaryResponse.builder().status("failed").build();
        LaunchSummaryResponse upcomingLaunch = LaunchSummaryResponse.builder().status("upcoming").build();
        List<LaunchSummaryResponse> allLaunches = Arrays.asList(successfulLaunch, successfulLaunch, failedLaunch, upcomingLaunch);

        when(launchService.getAllLaunches()).thenReturn(allLaunches);

        // When
        StatsDataResponse result = launchService.getLaunchStats();

        // Then
        assertEquals(4, result.getTotalLaunches());
        assertEquals(2, result.getSuccessfulLaunches());
        assertEquals(1, result.getFailedLaunches());
        assertEquals(1, result.getUpcomingLaunches());
        assertEquals(50.0, result.getSuccessRate(), 0.01);
    }

    @Test
    void getLaunchStats_WhenNoLaunches_ShouldReturnZeroStats() {
        // Given
        when(launchService.getAllLaunches()).thenReturn(Collections.emptyList());

        // When
        StatsDataResponse result = launchService.getLaunchStats();

        // Then
        assertEquals(0, result.getTotalLaunches());
        assertEquals(0, result.getSuccessfulLaunches());
        assertEquals(0, result.getFailedLaunches());
        assertEquals(0, result.getUpcomingLaunches());
        assertEquals(0.0, result.getSuccessRate(), 0.01);
    }
}