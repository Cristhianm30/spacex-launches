package io.github.cristhianm30.spacex_launches_back.application.service.impl;

import io.github.cristhianm30.spacex_launches_back.application.dto.response.LaunchResponse;
import io.github.cristhianm30.spacex_launches_back.application.dto.response.LaunchSummaryResponse;
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
import static org.mockito.ArgumentMatchers.anyList;
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
    void getSuccessfulLaunches_ShouldReturnMappedSuccessfulLaunches() {
        // Given
        List<LaunchModel> launches = Arrays.asList(launchModel);
        List<LaunchSummaryResponse> expectedResponses = Arrays.asList(launchSummaryResponse);
        
        when(useCasePort.getSuccessfulLaunches()).thenReturn(launches);
        when(mapper.toSummaryResponseList(launches)).thenReturn(expectedResponses);

        // When
        List<LaunchSummaryResponse> result = launchService.getSuccessfulLaunches();

        // Then
        assertEquals(expectedResponses, result);
        verify(useCasePort).getSuccessfulLaunches();
        verify(mapper).toSummaryResponseList(launches);
    }

    @Test
    void getFailedLaunches_ShouldReturnMappedFailedLaunches() {
        // Given
        LaunchModel failedLaunch = LaunchModel.builder()
                .launchId("2")
                .missionName("Failed Mission")
                .status("failed")
                .success(false)
                .build();
        
        List<LaunchModel> launches = Arrays.asList(failedLaunch);
        LaunchSummaryResponse failedResponse = LaunchSummaryResponse.builder()
                .launchId("2")
                .missionName("Failed Mission")
                .status("failed")
                .build();
        List<LaunchSummaryResponse> expectedResponses = Arrays.asList(failedResponse);
        
        when(useCasePort.getFailedLaunches()).thenReturn(launches);
        when(mapper.toSummaryResponseList(launches)).thenReturn(expectedResponses);

        // When
        List<LaunchSummaryResponse> result = launchService.getFailedLaunches();

        // Then
        assertEquals(expectedResponses, result);
        verify(useCasePort).getFailedLaunches();
        verify(mapper).toSummaryResponseList(launches);
    }

    @Test
    void getTotalLaunchesCount_ShouldReturnCorrectCount() {
        // Given
        List<LaunchModel> launches = Arrays.asList(launchModel, launchModel, launchModel);
        when(useCasePort.getAllLaunches()).thenReturn(launches);

        // When
        long result = launchService.getTotalLaunchesCount();

        // Then
        assertEquals(3, result);
        verify(useCasePort).getAllLaunches();
    }

    @Test
    void getTotalLaunchesCount_WhenNoLaunches_ShouldReturnZero() {
        // Given
        when(useCasePort.getAllLaunches()).thenReturn(Collections.emptyList());

        // When
        long result = launchService.getTotalLaunchesCount();

        // Then
        assertEquals(0, result);
        verify(useCasePort).getAllLaunches();
    }

    @Test
    void getSuccessRate_WithSuccessfulLaunches_ShouldReturnCorrectPercentage() {
        // Given
        LaunchSummaryResponse successfulLaunch = LaunchSummaryResponse.builder()
                .status("success")
                .build();
        LaunchSummaryResponse failedLaunch = LaunchSummaryResponse.builder()
                .status("failed")
                .build();
        
        List<LaunchSummaryResponse> allLaunches = Arrays.asList(successfulLaunch, failedLaunch, successfulLaunch);
        
        // Mock the getAllLaunches method call within getSuccessRate
        LaunchServiceImpl spyService = spy(launchService);
        doReturn(allLaunches).when(spyService).getAllLaunches();

        // When
        double result = spyService.getSuccessRate();

        // Then
        assertEquals(66.66666666666667, result, 0.001);
    }

    @Test
    void getSuccessRate_WithNoLaunches_ShouldReturnZero() {
        // Given
        LaunchServiceImpl spyService = spy(launchService);
        doReturn(Collections.emptyList()).when(spyService).getAllLaunches();

        // When
        double result = spyService.getSuccessRate();

        // Then
        assertEquals(0.0, result);
    }

    @Test
    void getSuccessRate_WithAllSuccessfulLaunches_ShouldReturnHundredPercent() {
        // Given
        LaunchSummaryResponse successfulLaunch1 = LaunchSummaryResponse.builder()
                .status("success")
                .build();
        LaunchSummaryResponse successfulLaunch2 = LaunchSummaryResponse.builder()
                .status("success")
                .build();
        
        List<LaunchSummaryResponse> allLaunches = Arrays.asList(successfulLaunch1, successfulLaunch2);
        
        LaunchServiceImpl spyService = spy(launchService);
        doReturn(allLaunches).when(spyService).getAllLaunches();

        // When
        double result = spyService.getSuccessRate();

        // Then
        assertEquals(100.0, result);
    }

    @Test
    void getSuccessRate_WithAllFailedLaunches_ShouldReturnZeroPercent() {
        // Given
        LaunchSummaryResponse failedLaunch1 = LaunchSummaryResponse.builder()
                .status("failed")
                .build();
        LaunchSummaryResponse failedLaunch2 = LaunchSummaryResponse.builder()
                .status("partial_failure")
                .build();
        
        List<LaunchSummaryResponse> allLaunches = Arrays.asList(failedLaunch1, failedLaunch2);
        
        LaunchServiceImpl spyService = spy(launchService);
        doReturn(allLaunches).when(spyService).getAllLaunches();

        // When
        double result = spyService.getSuccessRate();

        // Then
        assertEquals(0.0, result);
    }
}
