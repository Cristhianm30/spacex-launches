package io.github.cristhianm30.spacex_launches_back.infrastructure.persistence;

import io.github.cristhianm30.spacex_launches_back.domain.exception.DatabaseOperationException;
import io.github.cristhianm30.spacex_launches_back.domain.exception.InvalidParameterException;
import io.github.cristhianm30.spacex_launches_back.domain.util.constant.DatabaseConstants;
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
        this.table = enhancedClient.table(DatabaseConstants.SPACEX_LAUNCHES_TABLE, TableSchema.fromBean(LaunchEntity.class));
        this.mapper = mapper;
    }

    @Override
    public Optional<LaunchModel> findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidParameterException(DatabaseConstants.LAUNCH_ID_NULL_OR_EMPTY);
        }
        
        try {
            Key key = Key.builder().partitionValue(id).build();
            LaunchEntity entity = table.getItem(key);
            return Optional.ofNullable(entity).map(mapper::toDomain);
        } catch (Exception e) {
            throw new DatabaseOperationException(DatabaseConstants.ERROR_RETRIEVING_LAUNCH_BY_ID + id, e);
        }
    }

    @Override
    public List<LaunchModel> findAll() {
        try {
            return table.scan(ScanEnhancedRequest.builder().build())
                    .items()
                    .stream()
                    .map(mapper::toDomain)
                    .toList();
        } catch (Exception e) {
            throw new DatabaseOperationException(DatabaseConstants.ERROR_RETRIEVING_ALL_LAUNCHES, e);
        }
    }

    @Override
    public Page<LaunchModel> findAll(String status, Pageable pageable) {
        if (pageable == null) {
            throw new InvalidParameterException(DatabaseConstants.PAGEABLE_NULL);
        }
        
        if (pageable.getPageNumber() < 0 || pageable.getPageSize() <= 0) {
            throw new InvalidParameterException(DatabaseConstants.INVALID_PAGINATION_PARAMS);
        }
        
        try {
            List<LaunchModel> launches = findAll();

            if (status != null && !status.isEmpty()) {
                launches = launches.stream()
                        .filter(launch -> status.equals(launch.getStatus()))
                        .collect(Collectors.toList());
            }

            int start = pageable.getPageNumber() * pageable.getPageSize();
            int end = Math.min((start + pageable.getPageSize()), launches.size());

            return new Page<>(launches.subList(start, end), pageable.getPageNumber(), pageable.getPageSize(), launches.size());
        } catch (DatabaseOperationException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseOperationException(DatabaseConstants.ERROR_RETRIEVING_PAGINATED_LAUNCHES, e);
        }
    }

    @Override
    public List<LaunchModel> findByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new InvalidParameterException(DatabaseConstants.STATUS_NULL_OR_EMPTY);
        }
        
        try {
            return findAll()
                    .stream()
                    .filter(launch -> status.equals(launch.getStatus()))
                    .toList();
        } catch (DatabaseOperationException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseOperationException(DatabaseConstants.ERROR_RETRIEVING_LAUNCHES_BY_STATUS + status, e);
        }
    }

    @Override
    public List<LaunchModel> findByRocketId(String rocketId) {
        if (rocketId == null || rocketId.trim().isEmpty()) {
            throw new InvalidParameterException(DatabaseConstants.ROCKET_ID_NULL_OR_EMPTY);
        }
        
        try {
            return findAll()
                    .stream()
                    .filter(launch -> rocketId.equals(launch.getRocketId()))
                    .toList();
        } catch (DatabaseOperationException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseOperationException(DatabaseConstants.ERROR_RETRIEVING_LAUNCHES_BY_ROCKET_ID + rocketId, e);
        }
    }
}
