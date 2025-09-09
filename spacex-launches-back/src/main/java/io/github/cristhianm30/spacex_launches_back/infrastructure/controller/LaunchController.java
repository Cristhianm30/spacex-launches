package io.github.cristhianm30.spacex_launches_back.infrastructure.controller;

import io.github.cristhianm30.spacex_launches_back.application.dto.response.LaunchResponse;
import io.github.cristhianm30.spacex_launches_back.application.dto.response.LaunchSummaryResponse;
import io.github.cristhianm30.spacex_launches_back.application.dto.response.StatsDataResponse;
import io.github.cristhianm30.spacex_launches_back.application.service.api.LaunchService;
import io.github.cristhianm30.spacex_launches_back.domain.model.Page;
import io.github.cristhianm30.spacex_launches_back.domain.model.Pageable;
import io.github.cristhianm30.spacex_launches_back.domain.util.constant.EndpointConstants;
import io.github.cristhianm30.spacex_launches_back.domain.util.constant.LaunchStatusConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(EndpointConstants.LAUNCHES_BASE)
@CrossOrigin(origins = EndpointConstants.CORS_ORIGINS)
@RequiredArgsConstructor
@Tag(name = "Launches", description = "Operaciones de consulta de lanzamientos SpaceX")
public class LaunchController {

    private final LaunchService service;

    @GetMapping(EndpointConstants.ID_PATH)
    @Operation(summary = "Obtener detalle por ID", description = "Devuelve el detalle de un lanzamiento.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lanzamiento encontrado",
                    content = @Content(schema = @Schema(implementation = LaunchResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    public ResponseEntity<LaunchResponse> getLaunchById(@PathVariable String id) {
        return service.getLaunchById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Obtener todos los lanzamientos", description = "Devuelve una lista con todos los lanzamientos disponibles.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de lanzamientos obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = LaunchSummaryResponse.class))
            )
    })
    public ResponseEntity<List<LaunchSummaryResponse>> getAllLaunches() {
        return ResponseEntity.ok(service.getAllLaunches());
    }

    @GetMapping(EndpointConstants.PAGINATED)
    @Operation(summary = "Obtener lanzamientos paginados", description = "Devuelve una lista paginada de lanzamientos con filtro opcional por estado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista paginada obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = Page.class))
            )
    })
    public ResponseEntity<Page<LaunchSummaryResponse>> getLaunches(
            @Parameter(description = "Filtro opcional por estado del lanzamiento (success, failed, upcoming)")
            @RequestParam(required = false) String status,
            @Parameter(description = "Parámetros de paginación (page, size, sort)")
            Pageable pageable) {
        return ResponseEntity.ok(service.getLaunches(status, pageable));
    }

    @GetMapping(EndpointConstants.STATUS_PATH)
    @Operation(summary = "Obtener lanzamientos por estado", description = "Devuelve todos los lanzamientos que coinciden con el estado especificado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de lanzamientos por estado obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = LaunchSummaryResponse.class))
            )
    })
    public ResponseEntity<List<LaunchSummaryResponse>> getLaunchesByStatus(
            @Parameter(description = "Estado del lanzamiento (success, failed, upcoming)", required = true)
            @PathVariable String status) {
        return ResponseEntity.ok(service.getLaunchesByStatus(status));
    }

    @GetMapping(EndpointConstants.ROCKET_PATH)
    @Operation(summary = "Obtener lanzamientos por cohete", description = "Devuelve todos los lanzamientos realizados por un cohete específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de lanzamientos por cohete obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = LaunchSummaryResponse.class))
            )
    })
    public ResponseEntity<List<LaunchSummaryResponse>> getLaunchesByRocket(
            @Parameter(description = "ID del cohete", required = true)
            @PathVariable String rocketId) {
        return ResponseEntity.ok(service.getLaunchesByRocket(rocketId));
    }

    @GetMapping(EndpointConstants.SUCCESSFUL)
    @Operation(summary = "Obtener lanzamientos exitosos", description = "Devuelve todos los lanzamientos que fueron exitosos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de lanzamientos exitosos obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = LaunchSummaryResponse.class))
            )
    })
    public ResponseEntity<List<LaunchSummaryResponse>> getSuccessfulLaunches() {
        return ResponseEntity.ok(service.getLaunchesByStatus(LaunchStatusConstants.SUCCESS));
    }

    @GetMapping(EndpointConstants.FAILED)
    @Operation(summary = "Obtener lanzamientos fallidos", description = "Devuelve todos los lanzamientos que fallaron.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de lanzamientos fallidos obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = LaunchSummaryResponse.class))
            )
    })
    public ResponseEntity<List<LaunchSummaryResponse>> getFailedLaunches() {
        return ResponseEntity.ok(service.getLaunchesByStatus(LaunchStatusConstants.FAILED));
    }

    @GetMapping(EndpointConstants.STATS)
    @Operation(summary = "Obtener estadísticas de lanzamientos", description = "Devuelve estadísticas generales sobre todos los lanzamientos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente",
                    content = @Content(schema = @Schema(implementation = StatsDataResponse.class))
            )
    })
    public ResponseEntity<StatsDataResponse> getLaunchStats() {
        return ResponseEntity.ok(service.getLaunchStats());
    }
}
