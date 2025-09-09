package io.github.cristhianm30.spacex_launches_back.domain.util.constant;

public final class CorsConstants {
    
    private CorsConstants() {
        // Utility class
    }
    
    // CORS Mappings
    public static final String DOCS_MAPPING = "/docs/**";
    public static final String SWAGGER_UI_MAPPING = "/swagger-ui/**";
    
    // CORS Configuration
    public static final String ALLOWED_ORIGINS = "*";
    public static final String[] ALLOWED_METHODS = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
    public static final String ALLOWED_HEADERS = "*";
}
