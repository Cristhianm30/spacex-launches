package io.github.cristhianm30.spacex_launches_back.domain.util.constant;

public final class EndpointConstants {
    
    private EndpointConstants() {
    }
    
    public static final String LAUNCHES_BASE = "/launches";
    public static final String PAGINATED = "/paginated";
    public static final String STATUS_PATH = "/status/{status}";
    public static final String ROCKET_PATH = "/rocket/{rocketId}";
    public static final String SUCCESSFUL = "/successful";
    public static final String FAILED = "/failed";
    public static final String STATS = "/stats";
    public static final String ID_PATH = "/{id}";
    
    public static final String CORS_ORIGINS = "*";
}
