package io.github.cristhianm30.spacex_launches_back.infrastructure.entity;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;

@DynamoDbBean
@Data
public class LaunchEntity {
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

    @DynamoDbPartitionKey
    @DynamoDbAttribute("launch_id")
    public String getLaunchId() {
        return launchId;
    }

    @DynamoDbAttribute("mission_name")
    public String getMissionName() {
        return missionName;
    }

    @DynamoDbAttribute("flight_number")
    public Integer getFlightNumber() {
        return flightNumber;
    }

    @DynamoDbAttribute("launch_date_utc")
    public String getLaunchDateUtc() {
        return launchDateUtc;
    }

    @DynamoDbAttribute("rocket_id")
    public String getRocketId() {
        return rocketId;
    }

    @DynamoDbAttribute("launchpad_id")
    public String getLaunchpadId() {
        return launchpadId;
    }


    @DynamoDbAttribute("patch_small_link")
    public String getPatchSmallLink() {
        return patchSmallLink;
    }

    @DynamoDbAttribute("patch_large_link")
    public String getPatchLargeLink() {
        return patchLargeLink;
    }

    @DynamoDbAttribute("webcast_link")
    public String getWebcastLink() {
        return webcastLink;
    }

    @DynamoDbAttribute("article_link")
    public String getArticleLink() {
        return articleLink;
    }

    @DynamoDbAttribute("wikipedia_link")
    public String getWikipediaLink() {
        return wikipediaLink;
    }

}
