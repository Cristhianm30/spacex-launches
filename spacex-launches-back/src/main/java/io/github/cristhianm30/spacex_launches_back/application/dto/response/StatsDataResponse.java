package io.github.cristhianm30.spacex_launches_back.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Estadísticas de lanzamientos SpaceX")
public class StatsDataResponse {

    @Schema(description = "Número total de lanzamientos", example = "150")
    private long totalLaunches;
    
    @Schema(description = "Tasa de éxito en porcentaje", example = "92.5")
    private double successRate;
    
    @Schema(description = "Número de lanzamientos exitosos", example = "139")
    private int successfulLaunches;
    
    @Schema(description = "Número de lanzamientos fallidos", example = "11")
    private int failedLaunches;
    
    @Schema(description = "Número de lanzamientos próximos", example = "5")
    private int upcomingLaunches;
}
