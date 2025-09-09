package io.github.cristhianm30.spacex_launches_back.infrastructure.mapper;

import io.github.cristhianm30.spacex_launches_back.domain.model.LaunchModel;
import io.github.cristhianm30.spacex_launches_back.infrastructure.entity.LaunchEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface LaunchEntityMapper {

    LaunchModel toDomain(LaunchEntity entity);

    List<LaunchModel> toDomainList(List<LaunchEntity> entityList);

    LaunchEntity toEntity(LaunchModel domain);
}