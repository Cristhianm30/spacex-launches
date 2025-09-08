package io.github.cristhianm30.spacex_launches_back.domain.port.out;

import io.github.cristhianm30.spacex_launches_back.domain.model.LaunchModel;
import io.github.cristhianm30.spacex_launches_back.domain.model.Page;
import io.github.cristhianm30.spacex_launches_back.domain.model.Pageable;

import java.util.List;
import java.util.Optional;

public interface LaunchRepositoryPort {
    Optional<LaunchModel> findById(String id);
    List<LaunchModel> findAll();
    Page<LaunchModel> findAll(String status, Pageable pageable);
    List<LaunchModel> findByStatus(String status);
    List<LaunchModel> findByRocketId(String rocketId);
}
