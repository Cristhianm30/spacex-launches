package io.github.cristhianm30.spacex_launches_back.domain.util.constant;

public final class CorsConstants {
    
    private CorsConstants() {
    }
    
    public static final String DOCS_MAPPING = "/docs/**";
    public static final String SWAGGER_UI_MAPPING = "/swagger-ui/**";
    public static final String SWAGGER_CONFIG_MAPPING = "/swagger-config/**";
    public static final String V3_API_DOCS_MAPPING = "/v3/api-docs/**";
    
    public static final String ALLOWED_ORIGINS = "*";
    public static final String[] ALLOWED_METHODS = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
    public static final String ALLOWED_HEADERS = "*";
}
