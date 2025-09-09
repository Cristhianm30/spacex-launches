package io.github.cristhianm30.spacex_launches_back.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta detallada de un lanzamiento SpaceX")
public class LaunchResponse {
    @Schema(description = "ID único del lanzamiento", example = "5eb87cd9ffd86e000604b32a")
    private String launchId;
    
    @Schema(description = "Nombre de la misión", example = "FalconSat")
    private String missionName;
    
    @Schema(description = "Número de vuelo", example = "1")
    private Integer flightNumber;
    
    @Schema(description = "Fecha y hora del lanzamiento en UTC", example = "2006-03-24T22:30:00.000Z")
    private String launchDateUtc;
    
    @Schema(description = "Indica si el lanzamiento fue exitoso", example = "false")
    private Boolean success;
    
    @Schema(description = "Detalles adicionales del lanzamiento")
    private String details;
    
    @Schema(description = "ID del cohete utilizado", example = "5e9d0d95eda69955f709d1eb")
    private String rocketId;
    
    @Schema(description = "ID de la plataforma de lanzamiento", example = "5e9e4502f5090995de566f86")
    private String launchpadId;
    
    @Schema(description = "Lista de IDs de cargas útiles")
    private List<String> payloads;
    
    @Schema(description = "Enlace al parche pequeño de la misión")
    private String patchSmallLink;
    
    @Schema(description = "Enlace al parche grande de la misión")
    private String patchLargeLink;
    
    @Schema(description = "Enlace al webcast del lanzamiento")
    private String webcastLink;
    
    @Schema(description = "Enlace al artículo del lanzamiento")
    private String articleLink;
    
    @Schema(description = "Enlace a Wikipedia del lanzamiento")
    private String wikipediaLink;
    
    @Schema(description = "Estado del lanzamiento", example = "success", allowableValues = {"success", "failed", "upcoming"})
    private String status;
}
