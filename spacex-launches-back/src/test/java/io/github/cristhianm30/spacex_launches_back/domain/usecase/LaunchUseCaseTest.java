package io.github.cristhianm30.spacex_launches_back.domain.usecase;

import io.github.cristhianm30.spacex_launches_back.domain.model.LaunchModel;
import io.github.cristhianm30.spacex_launches_back.domain.port.out.LaunchRepositoryPort;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LaunchUseCaseTest {

    @Mock
    private LaunchRepositoryPort repositoryPort;

    @InjectMocks
    private LaunchUseCase launchUseCase;

    private LaunchModel successfulLaunch;
    private LaunchModel failedLaunch;
    private LaunchModel partialFailureLaunch;

    @BeforeEach
    void setUp() {
        successfulLaunch = LaunchModel.builder()
                .launchId("1")
                .missionName("Successful Mission")
                .flightNumber(1)
                .status("success")
                .success(true)
                .rocketId("falcon9")
                .build();

        failedLaunch = LaunchModel.builder()
                .launchId("2")
                .missionName("Failed Mission")
                .flightNumber(2)
                .status("failed")
                .success(false)
                .rocketId("falcon9")
                .build();

        partialFailureLaunch = LaunchModel.builder()
                .launchId("3")
                .missionName("Partial Failure Mission")
                .flightNumber(3)
                .status("partial_failure")
                .success(false)
                .rocketId("falconheavy")
                .build();
    }

    @Test
    void getLaunchById_WhenLaunchExists_ShouldReturnLaunch() {
        // Given
        String launchId = "1";
        when(repositoryPort.findById(launchId)).thenReturn(Optional.of(successfulLaunch));

        // When
        Optional<LaunchModel> result = launchUseCase.getLaunchById(launchId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(successfulLaunch, result.get());
        verify(repositoryPort).findById(launchId);
    }

    @Test
    void getLaunchById_WhenLaunchNotExists_ShouldReturnEmpty() {
        // Given
        String launchId = "nonexistent";
        when(repositoryPort.findById(launchId)).thenReturn(Optional.empty());

        // When
        Optional<LaunchModel> result = launchUseCase.getLaunchById(launchId);

        // Then
        assertFalse(result.isPresent());
        verify(repositoryPort).findById(launchId);
    }

    @Test
    void getAllLaunches_ShouldReturnAllLaunches() {
        // Given
        List<LaunchModel> allLaunches = Arrays.asList(successfulLaunch, failedLaunch, partialFailureLaunch);
        when(repositoryPort.findAll()).thenReturn(allLaunches);

        // When
        List<LaunchModel> result = launchUseCase.getAllLaunches();

        // Then
        assertEquals(allLaunches, result);
        assertEquals(3, result.size());
        verify(repositoryPort).findAll();
    }

    @Test
    void getAllLaunches_WhenNoLaunches_ShouldReturnEmptyList() {
        // Given
        when(repositoryPort.findAll()).thenReturn(Collections.emptyList());

        // When
        List<LaunchModel> result = launchUseCase.getAllLaunches();

        // Then
        assertTrue(result.isEmpty());
        verify(repositoryPort).findAll();
    }

    @Test
    void getLaunchesByStatus_ShouldReturnFilteredLaunches() {
        // Given
        String status = "success";
        List<LaunchModel> successfulLaunches = Arrays.asList(successfulLaunch);
        when(repositoryPort.findByStatus(status)).thenReturn(successfulLaunches);

        // When
        List<LaunchModel> result = launchUseCase.getLaunchesByStatus(status);

        // Then
        assertEquals(successfulLaunches, result);
        assertEquals(1, result.size());
        assertEquals("success", result.get(0).getStatus());
        verify(repositoryPort).findByStatus(status);
    }

    @Test
    void getLaunchesByStatus_WhenNoMatchingStatus_ShouldReturnEmptyList() {
        // Given
        String status = "nonexistent_status";
        when(repositoryPort.findByStatus(status)).thenReturn(Collections.emptyList());

        // When
        List<LaunchModel> result = launchUseCase.getLaunchesByStatus(status);

        // Then
        assertTrue(result.isEmpty());
        verify(repositoryPort).findByStatus(status);
    }

    @Test
    void getLaunchesByRocket_ShouldReturnFilteredLaunches() {
        // Given
        String rocketId = "falcon9";
        List<LaunchModel> falcon9Launches = Arrays.asList(successfulLaunch, failedLaunch);
        when(repositoryPort.findByRocketId(rocketId)).thenReturn(falcon9Launches);

        // When
        List<LaunchModel> result = launchUseCase.getLaunchesByRocket(rocketId);

        // Then
        assertEquals(falcon9Launches, result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(launch -> "falcon9".equals(launch.getRocketId())));
        verify(repositoryPort).findByRocketId(rocketId);
    }

    @Test
    void getLaunchesByRocket_WhenNoMatchingRocket_ShouldReturnEmptyList() {
        // Given
        String rocketId = "nonexistent_rocket";
        when(repositoryPort.findByRocketId(rocketId)).thenReturn(Collections.emptyList());

        // When
        List<LaunchModel> result = launchUseCase.getLaunchesByRocket(rocketId);

        // Then
        assertTrue(result.isEmpty());
        verify(repositoryPort).findByRocketId(rocketId);
    }

    @Test
    void getSuccessfulLaunches_ShouldReturnOnlySuccessfulLaunches() {
        // Given
        List<LaunchModel> successfulLaunches = Arrays.asList(successfulLaunch);
        when(repositoryPort.findByStatus("success")).thenReturn(successfulLaunches);

        // When
        List<LaunchModel> result = launchUseCase.getSuccessfulLaunches();

        // Then
        assertEquals(successfulLaunches, result);
        assertEquals(1, result.size());
        assertEquals("success", result.get(0).getStatus());
        verify(repositoryPort).findByStatus("success");
    }

    @Test
    void getSuccessfulLaunches_WhenNoSuccessfulLaunches_ShouldReturnEmptyList() {
        // Given
        when(repositoryPort.findByStatus("success")).thenReturn(Collections.emptyList());

        // When
        List<LaunchModel> result = launchUseCase.getSuccessfulLaunches();

        // Then
        assertTrue(result.isEmpty());
        verify(repositoryPort).findByStatus("success");
    }

    @Test
    void getFailedLaunches_ShouldReturnNonSuccessfulLaunches() {
        // Given
        List<LaunchModel> allLaunches = Arrays.asList(successfulLaunch, failedLaunch, partialFailureLaunch);
        when(repositoryPort.findAll()).thenReturn(allLaunches);

        // When
        List<LaunchModel> result = launchUseCase.getFailedLaunches();

        // Then
        assertEquals(2, result.size());
        assertTrue(result.contains(failedLaunch));
        assertTrue(result.contains(partialFailureLaunch));
        assertFalse(result.contains(successfulLaunch));
        assertTrue(result.stream().noneMatch(launch -> "success".equals(launch.getStatus())));
        verify(repositoryPort).findAll();
    }

    @Test
    void getFailedLaunches_WhenAllLaunchesSuccessful_ShouldReturnEmptyList() {
        // Given
        List<LaunchModel> allSuccessfulLaunches = Arrays.asList(successfulLaunch);
        when(repositoryPort.findAll()).thenReturn(allSuccessfulLaunches);

        // When
        List<LaunchModel> result = launchUseCase.getFailedLaunches();

        // Then
        assertTrue(result.isEmpty());
        verify(repositoryPort).findAll();
    }

    @Test
    void getFailedLaunches_WhenNoLaunches_ShouldReturnEmptyList() {
        // Given
        when(repositoryPort.findAll()).thenReturn(Collections.emptyList());

        // When
        List<LaunchModel> result = launchUseCase.getFailedLaunches();

        // Then
        assertTrue(result.isEmpty());
        verify(repositoryPort).findAll();
    }

    @Test
    void getFailedLaunches_ShouldFilterOutSuccessStatus() {
        // Given
        LaunchModel anotherSuccessfulLaunch = LaunchModel.builder()
                .launchId("4")
                .missionName("Another Successful Mission")
                .status("success")
                .build();

        List<LaunchModel> mixedLaunches = Arrays.asList(
                successfulLaunch, 
                failedLaunch, 
                partialFailureLaunch, 
                anotherSuccessfulLaunch
        );
        when(repositoryPort.findAll()).thenReturn(mixedLaunches);

        // When
        List<LaunchModel> result = launchUseCase.getFailedLaunches();

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().noneMatch(launch -> "success".equals(launch.getStatus())));
        assertTrue(result.stream().anyMatch(launch -> "failed".equals(launch.getStatus())));
        assertTrue(result.stream().anyMatch(launch -> "partial_failure".equals(launch.getStatus())));
        verify(repositoryPort).findAll();
    }
}
