package io.github.cristhianm30.spacex_launches_back.domain.util.constant;

public final class ApiConstants {
    
    private ApiConstants() {
    }
    
    public static final String API_TITLE = "SpaceX Launches API";
    public static final String API_VERSION = "v1";
    public static final String API_DESCRIPTION = "API para consultar lanzamientos SpaceX almacenados en DynamoDB";
    
    public static final String CONTACT_NAME = "Cristhian Moreno";
    public static final String CONTACT_EMAIL = "";
    
    public static final String LICENSE_NAME = "MIT";
    
    public static final String BASE_SERVER_URL = "/api";
    public static final String BASE_SERVER_DESCRIPTION = "Base path detrás de API Gateway o reverse proxy";
    public static final String PRODUCTION_SERVER_URL = "https://lbs33m5sf6.execute-api.us-east-1.amazonaws.com/prod/api";
    public static final String PRODUCTION_SERVER_DESCRIPTION = "Production";
}
