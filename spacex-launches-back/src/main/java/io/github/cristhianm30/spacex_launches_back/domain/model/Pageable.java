package io.github.cristhianm30.spacex_launches_back.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pageable {

    private int pageNumber;
    private int pageSize;
}
