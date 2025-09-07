package io.github.cristhianm30.spacex_launches_back.domain.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LaunchModelTest {

    @Test
    void launchModel_ShouldCreateInstanceWithBuilder() {
        // Given
        String launchId = "launch123";
        String missionName = "Test Mission";
        Integer flightNumber = 42;
        String launchDateUtc = "2023-12-01T10:00:00.000Z";
        Boolean success = true;
        String details = "Test launch details";
        String rocketId = "falcon9";
        String launchpadId = "launchpad1";
        List<String> payloads = Arrays.asList("payload1", "payload2");
        String patchSmallLink = "https://example.com/patch-small.png";
        String patchLargeLink = "https://example.com/patch-large.png";
        String webcastLink = "https://example.com/webcast";
        String articleLink = "https://example.com/article";
        String wikipediaLink = "https://example.com/wikipedia";
        String status = "success";

        // When
        LaunchModel launchModel = LaunchModel.builder()
                .launchId(launchId)
                .missionName(missionName)
                .flightNumber(flightNumber)
                .launchDateUtc(launchDateUtc)
                .success(success)
                .details(details)
                .rocketId(rocketId)
                .launchpadId(launchpadId)
                .payloads(payloads)
                .patchSmallLink(patchSmallLink)
                .patchLargeLink(patchLargeLink)
                .webcastLink(webcastLink)
                .articleLink(articleLink)
                .wikipediaLink(wikipediaLink)
                .status(status)
                .build();

        // Then
        assertEquals(launchId, launchModel.getLaunchId());
        assertEquals(missionName, launchModel.getMissionName());
        assertEquals(flightNumber, launchModel.getFlightNumber());
        assertEquals(launchDateUtc, launchModel.getLaunchDateUtc());
        assertEquals(success, launchModel.getSuccess());
        assertEquals(details, launchModel.getDetails());
        assertEquals(rocketId, launchModel.getRocketId());
        assertEquals(launchpadId, launchModel.getLaunchpadId());
        assertEquals(payloads, launchModel.getPayloads());
        assertEquals(patchSmallLink, launchModel.getPatchSmallLink());
        assertEquals(patchLargeLink, launchModel.getPatchLargeLink());
        assertEquals(webcastLink, launchModel.getWebcastLink());
        assertEquals(articleLink, launchModel.getArticleLink());
        assertEquals(wikipediaLink, launchModel.getWikipediaLink());
        assertEquals(status, launchModel.getStatus());
    }

    @Test
    void launchModel_ShouldCreateInstanceWithNoArgsConstructor() {
        // When
        LaunchModel launchModel = new LaunchModel();

        // Then
        assertNotNull(launchModel);
        assertNull(launchModel.getLaunchId());
        assertNull(launchModel.getMissionName());
        assertNull(launchModel.getFlightNumber());
        assertNull(launchModel.getLaunchDateUtc());
        assertNull(launchModel.getSuccess());
        assertNull(launchModel.getDetails());
        assertNull(launchModel.getRocketId());
        assertNull(launchModel.getLaunchpadId());
        assertNull(launchModel.getPayloads());
        assertNull(launchModel.getPatchSmallLink());
        assertNull(launchModel.getPatchLargeLink());
        assertNull(launchModel.getWebcastLink());
        assertNull(launchModel.getArticleLink());
        assertNull(launchModel.getWikipediaLink());
        assertNull(launchModel.getStatus());
    }

    @Test
    void launchModel_ShouldCreateInstanceWithAllArgsConstructor() {
        // Given
        String launchId = "launch456";
        String missionName = "Constructor Test Mission";
        Integer flightNumber = 100;
        String launchDateUtc = "2024-01-01T12:00:00.000Z";
        Boolean success = false;
        String details = "Constructor test details";
        String rocketId = "falconheavy";
        String launchpadId = "launchpad2";
        List<String> payloads = Arrays.asList("payload3", "payload4", "payload5");
        String patchSmallLink = "https://example.com/constructor-patch-small.png";
        String patchLargeLink = "https://example.com/constructor-patch-large.png";
        String webcastLink = "https://example.com/constructor-webcast";
        String articleLink = "https://example.com/constructor-article";
        String wikipediaLink = "https://example.com/constructor-wikipedia";
        String status = "failed";

        // When
        LaunchModel launchModel = new LaunchModel(
                launchId, missionName, flightNumber, launchDateUtc, success, details,
                rocketId, launchpadId, payloads, patchSmallLink, patchLargeLink,
                webcastLink, articleLink, wikipediaLink, status
        );

        // Then
        assertEquals(launchId, launchModel.getLaunchId());
        assertEquals(missionName, launchModel.getMissionName());
        assertEquals(flightNumber, launchModel.getFlightNumber());
        assertEquals(launchDateUtc, launchModel.getLaunchDateUtc());
        assertEquals(success, launchModel.getSuccess());
        assertEquals(details, launchModel.getDetails());
        assertEquals(rocketId, launchModel.getRocketId());
        assertEquals(launchpadId, launchModel.getLaunchpadId());
        assertEquals(payloads, launchModel.getPayloads());
        assertEquals(patchSmallLink, launchModel.getPatchSmallLink());
        assertEquals(patchLargeLink, launchModel.getPatchLargeLink());
        assertEquals(webcastLink, launchModel.getWebcastLink());
        assertEquals(articleLink, launchModel.getArticleLink());
        assertEquals(wikipediaLink, launchModel.getWikipediaLink());
        assertEquals(status, launchModel.getStatus());
    }

    @Test
    void launchModel_ShouldAllowSettingFields() {
        // Given
        LaunchModel launchModel = new LaunchModel();
        String launchId = "setter-test-123";
        String missionName = "Setter Test Mission";
        Integer flightNumber = 999;
        Boolean success = true;
        String status = "success";

        // When
        launchModel.setLaunchId(launchId);
        launchModel.setMissionName(missionName);
        launchModel.setFlightNumber(flightNumber);
        launchModel.setSuccess(success);
        launchModel.setStatus(status);

        // Then
        assertEquals(launchId, launchModel.getLaunchId());
        assertEquals(missionName, launchModel.getMissionName());
        assertEquals(flightNumber, launchModel.getFlightNumber());
        assertEquals(success, launchModel.getSuccess());
        assertEquals(status, launchModel.getStatus());
    }

    @Test
    void launchModel_ShouldSupportEqualsAndHashCode() {
        // Given
        LaunchModel launch1 = LaunchModel.builder()
                .launchId("123")
                .missionName("Test Mission")
                .flightNumber(1)
                .success(true)
                .status("success")
                .build();

        LaunchModel launch2 = LaunchModel.builder()
                .launchId("123")
                .missionName("Test Mission")
                .flightNumber(1)
                .success(true)
                .status("success")
                .build();

        LaunchModel launch3 = LaunchModel.builder()
                .launchId("456")
                .missionName("Different Mission")
                .flightNumber(2)
                .success(false)
                .status("failed")
                .build();

        // Then
        assertEquals(launch1, launch2);
        assertNotEquals(launch1, launch3);
        assertEquals(launch1.hashCode(), launch2.hashCode());
        assertNotEquals(launch1.hashCode(), launch3.hashCode());
    }

    @Test
    void launchModel_ShouldSupportToString() {
        // Given
        LaunchModel launchModel = LaunchModel.builder()
                .launchId("toString-test")
                .missionName("ToString Test Mission")
                .flightNumber(42)
                .success(true)
                .status("success")
                .build();

        // When
        String toString = launchModel.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("toString-test"));
        assertTrue(toString.contains("ToString Test Mission"));
        assertTrue(toString.contains("42"));
        assertTrue(toString.contains("success"));
    }

    @Test
    void launchModel_ShouldHandleNullPayloads() {
        // Given & When
        LaunchModel launchModel = LaunchModel.builder()
                .launchId("null-payload-test")
                .missionName("Null Payload Test")
                .payloads(null)
                .build();

        // Then
        assertNull(launchModel.getPayloads());
    }

    @Test
    void launchModel_ShouldHandleEmptyPayloads() {
        // Given
        List<String> emptyPayloads = Arrays.asList();

        // When
        LaunchModel launchModel = LaunchModel.builder()
                .launchId("empty-payload-test")
                .missionName("Empty Payload Test")
                .payloads(emptyPayloads)
                .build();

        // Then
        assertNotNull(launchModel.getPayloads());
        assertTrue(launchModel.getPayloads().isEmpty());
    }

    @Test
    void launchModel_ShouldHandleNullBooleanFields() {
        // Given & When
        LaunchModel launchModel = LaunchModel.builder()
                .launchId("null-boolean-test")
                .missionName("Null Boolean Test")
                .success(null)
                .build();

        // Then
        assertNull(launchModel.getSuccess());
    }

    @Test
    void launchModel_ShouldHandleNullIntegerFields() {
        // Given & When
        LaunchModel launchModel = LaunchModel.builder()
                .launchId("null-integer-test")
                .missionName("Null Integer Test")
                .flightNumber(null)
                .build();

        // Then
        assertNull(launchModel.getFlightNumber());
    }
}
