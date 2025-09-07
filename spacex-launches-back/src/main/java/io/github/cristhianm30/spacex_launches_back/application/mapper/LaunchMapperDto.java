package io.github.cristhianm30.spacex_launches_back.application.mapper;

import io.github.cristhianm30.spacex_launches_back.application.dto.response.LaunchResponse;
import io.github.cristhianm30.spacex_launches_back.application.dto.response.LaunchSummaryResponse;
import io.github.cristhianm30.spacex_launches_back.domain.model.LaunchModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface LaunchMapperDto {
    
    LaunchResponse toResponse(LaunchModel domain);
    
    List<LaunchResponse> toResponseList(List<LaunchModel> domainList);
    
    LaunchSummaryResponse toSummaryResponse(LaunchModel domain);
    
    List<LaunchSummaryResponse> toSummaryResponseList(List<LaunchModel> domainList);
}