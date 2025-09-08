package io.github.cristhianm30.spacex_launches_back.infrastructure.persistence;

import io.github.cristhianm30.spacex_launches_back.domain.model.LaunchModel;
import io.github.cristhianm30.spacex_launches_back.domain.model.Page;
import io.github.cristhianm30.spacex_launches_back.domain.model.Pageable;
import io.github.cristhianm30.spacex_launches_back.domain.port.out.LaunchRepositoryPort;
import io.github.cristhianm30.spacex_launches_back.infrastructure.entity.LaunchEntity;
import io.github.cristhianm30.spacex_launches_back.infrastructure.mapper.LaunchEntityMapper;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class DynamoDbLaunchRepository implements LaunchRepositoryPort {

    private final DynamoDbTable<LaunchEntity> table;
    private final LaunchEntityMapper mapper;

    public DynamoDbLaunchRepository(DynamoDbEnhancedClient enhancedClient,
                                    LaunchEntityMapper mapper) {
        this.table = enhancedClient.table("spacex-launches", TableSchema.fromBean(LaunchEntity.class));
        this.mapper = mapper;
    }

    @Override
    public Optional<LaunchModel> findById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        LaunchEntity entity = table.getItem(key);
        return Optional.ofNullable(entity).map(mapper::toDomain);
    }

    @Override
    public List<LaunchModel> findAll() {
        return table.scan(ScanEnhancedRequest.builder().build())
                .items()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Page<LaunchModel> findAll(String status, Pageable pageable) {
        List<LaunchModel> launches = findAll();

        if (status != null && !status.isEmpty()) {
            launches = launches.stream()
                    .filter(launch -> status.equals(launch.getStatus()))
                    .collect(Collectors.toList());
        }

        int start = pageable.getPageNumber() * pageable.getPageSize();
        int end = Math.min((start + pageable.getPageSize()), launches.size());

        return new Page<>(launches.subList(start, end), pageable.getPageNumber(), pageable.getPageSize(), launches.size());
    }

    @Override
    public List<LaunchModel> findByStatus(String status) {
        return findAll()
                .stream()
                .filter(launch -> status.equals(launch.getStatus()))
                .toList();
    }

    @Override
    public List<LaunchModel> findByRocketId(String rocketId) {
        return findAll()
                .stream()
                .filter(launch -> rocketId.equals(launch.getRocketId()))
                .toList();
    }
}
