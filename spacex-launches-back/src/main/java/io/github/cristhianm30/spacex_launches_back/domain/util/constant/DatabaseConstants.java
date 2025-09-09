package io.github.cristhianm30.spacex_launches_back.domain.util.constant;

public final class DatabaseConstants {
    
    private DatabaseConstants() {
    }
    
    public static final String SPACEX_LAUNCHES_TABLE = "spacex-launches";
    
    // Error Messages
    public static final String LAUNCH_ID_NULL_OR_EMPTY = "Launch ID cannot be null or empty";
    public static final String PAGEABLE_NULL = "Pageable cannot be null";
    public static final String INVALID_PAGINATION_PARAMS = "Invalid pagination parameters: page number must be >= 0 and page size must be > 0";
    public static final String STATUS_NULL_OR_EMPTY = "Status cannot be null or empty";
    public static final String ROCKET_ID_NULL_OR_EMPTY = "Rocket ID cannot be null or empty";
    
    public static final String ERROR_RETRIEVING_LAUNCH_BY_ID = "Error retrieving launch with ID: ";
    public static final String ERROR_RETRIEVING_ALL_LAUNCHES = "Error retrieving all launches";
    public static final String ERROR_RETRIEVING_PAGINATED_LAUNCHES = "Error retrieving paginated launches";
    public static final String ERROR_RETRIEVING_LAUNCHES_BY_STATUS = "Error retrieving launches by status: ";
    public static final String ERROR_RETRIEVING_LAUNCHES_BY_ROCKET_ID = "Error retrieving launches by rocket ID: ";
}
