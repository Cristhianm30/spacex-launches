package io.github.cristhianm30.spacex_launches_back.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Page<T> {

    private List<T> content;
    private int number;
    private int size;
    private long totalElements;

    public int getTotalPages() {
        return size == 0 ? 1 : (int) Math.ceil((double) totalElements / (double) size);
    }

    public boolean isLast() {
        return number + 1 >= getTotalPages();
    }

    public boolean isFirst() {
        return number == 0;
    }
}
