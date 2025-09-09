package io.github.cristhianm30.spacex_launches_back.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resumen de un lanzamiento SpaceX")
public class LaunchSummaryResponse {
    @Schema(description = "ID único del lanzamiento", example = "5eb87cd9ffd86e000604b32a")
    private String launchId;
    
    @Schema(description = "Nombre de la misión", example = "FalconSat")
    private String missionName;
    
    @Schema(description = "Número de vuelo", example = "1")
    private Integer flightNumber;
    
    @Schema(description = "Fecha y hora del lanzamiento en UTC", example = "2006-03-24T22:30:00.000Z")
    private String launchDateUtc;
    
    @Schema(description = "Estado del lanzamiento", example = "success", allowableValues = {"success", "failed", "upcoming"})
    private String status;
    
    @Schema(description = "ID del cohete utilizado", example = "5e9d0d95eda69955f709d1eb")
    private String rocketId;
}
