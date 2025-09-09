# SpaceX Launches API

Una API REST para consultar información sobre lanzamientos de SpaceX almacenados en DynamoDB.

## 🚀 Descripción

Esta API proporciona endpoints para consultar datos históricos y estadísticas de lanzamientos de SpaceX. Los datos se almacenan en Amazon DynamoDB y la API está construida con Spring Boot.

## 📋 Características

- ✅ Consulta de lanzamientos por ID
- ✅ Listado completo de lanzamientos
- ✅ Paginación de resultados
- ✅ Filtrado por estado (exitoso, fallido, próximo)
- ✅ Filtrado por cohete
- ✅ Estadísticas generales
- ✅ Documentación Swagger/OpenAPI
- ✅ CORS habilitado
- ✅ Despliegue en ECS Fargate

## 🛠️ Tecnologías

- **Java 21**
- **Spring Boot 3.5.5**
- **Spring Web** (MVC)
- **Amazon DynamoDB** (AWS SDK 2.33.4)
- **SpringDoc OpenAPI 2.5.0**
- **MapStruct 1.6.3**
- **Lombok**
- **Gradle 8.5.0**
- **Docker** (Eclipse Temurin JRE 21)

## 📚 Documentación API

La documentación interactiva de la API está disponible en:

- **Local**: `http://localhost:8080/api/swagger-ui`
- **Producción**: `https://lbs33m5sf6.execute-api.us-east-1.amazonaws.com/prod/api/swagger-ui`

## 🔗 Endpoints

### Lanzamientos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/launches` | Obtiene todos los lanzamientos |
| `GET` | `/api/launches/{id}` | Obtiene un lanzamiento por ID |
| `GET` | `/api/launches/paginated` | Obtiene lanzamientos paginados |
| `GET` | `/api/launches/status/{status}` | Obtiene lanzamientos por estado |
| `GET` | `/api/launches/rocket/{rocketId}` | Obtiene lanzamientos por cohete |
| `GET` | `/api/launches/successful` | Obtiene lanzamientos exitosos |
| `GET` | `/api/launches/failed` | Obtiene lanzamientos fallidos |
| `GET` | `/api/launches/stats` | Obtiene estadísticas generales |

### Parámetros de Consulta

#### Paginación (`/api/launches/paginated`)
- `status` (opcional): Filtro por estado (`success`, `failed`, `upcoming`)
- `page` (opcional): Número de página (default: 0)
- `size` (opcional): Tamaño de página (default: 20)
- `sort` (opcional): Campo de ordenamiento

### Ejemplos de Uso

#### Obtener un lanzamiento específico
```bash
curl -X GET "hhttps://lbs33m5sf6.execute-api.us-east-1.amazonaws.com/prod/api/launches/5eb87cd9ffd86e000604b32a"
```

#### Obtener lanzamientos paginados con filtro
```bash
curl -X GET "https://lbs33m5sf6.execute-api.us-east-1.amazonaws.com/prod/api/launches/paginated?status=success&page=0&size=10"
```

#### Obtener estadísticas
```bash
curl -X GET "https://lbs33m5sf6.execute-api.us-east-1.amazonaws.com/prod/api/launches/stats"
```

## 📊 Modelos de Datos

### LaunchResponse
Respuesta detallada de un lanzamiento:
```json
{
  "launchId": "5eb87cd9ffd86e000604b32a",
  "missionName": "FalconSat",
  "flightNumber": 1,
  "launchDateUtc": "2006-03-24T22:30:00.000Z",
  "success": false,
  "details": "Engine failure at 33 seconds and loss of vehicle",
  "rocketId": "5e9d0d95eda69955f709d1eb",
  "launchpadId": "5e9e4502f5090995de566f86",
  "payloads": ["5eb0e4b5b6c3bb0006eeb1e1"],
  "patchSmallLink": "https://images2.imgbox.com/3c/0e/T8iJcSN3_o.png",
  "patchLargeLink": "https://images2.imgbox.com/40/e3/GypSkayF_o.png",
  "webcastLink": "https://www.youtube.com/watch?v=0a_00nJ_Y88",
  "articleLink": "https://www.space.com/2196-spacex-inaugural-falcon-1-rocket-lost-launch.html",
  "wikipediaLink": "https://en.wikipedia.org/wiki/DemoSat",
  "status": "failed"
}
```

### LaunchSummaryResponse
Resumen de un lanzamiento:
```json
{
  "launchId": "5eb87cd9ffd86e000604b32a",
  "missionName": "FalconSat",
  "flightNumber": 1,
  "launchDateUtc": "2006-03-24T22:30:00.000Z",
  "status": "failed",
  "rocketId": "5e9d0d95eda69955f709d1eb"
}
```

### StatsDataResponse
Estadísticas de lanzamientos:
```json
{
  "totalLaunches": 150,
  "successRate": 92.5,
  "successfulLaunches": 139,
  "failedLaunches": 11,
  "upcomingLaunches": 5
}
```

## 🚀 Instalación y Ejecución

### Prerrequisitos
- Java 21 o superior
- Gradle 8.5.0 o superior
- Docker (opcional)
- AWS CLI configurado (para despliegue)

### Ejecución Local

1. **Clonar el repositorio**
```bash
git clone <repository-url>
cd spacex-launches-back
```

2. **Ejecutar con Gradle**
```bash
./gradlew bootRun
```

3. **Ejecutar con Docker**
```bash
docker build -t spacex-launches-api .
docker run -p 8080:8080 spacex-launches-api
```

La API estará disponible en `http://localhost:8080/api`

### Configuración de Perfiles

#### Desarrollo (`application.yml`)
```yaml
spring:
  application:
    name: spacex-launches-back
  profiles:
    active: prod
```

#### Producción (`application-prod.yml`)
```yaml
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  config:
    activate:
      on-profile: prod

springdoc:
  api-docs:
    path: /docs
  swagger-ui:
    enabled: true
    path: /swagger-ui
```

## 🏗️ Arquitectura

El proyecto sigue una arquitectura hexagonal (Clean Architecture):

```
src/
├── main/java/io/github/cristhianm30/spacex_launches_back/
│   ├── application/           # Casos de uso y servicios
│   │   ├── dto/              # DTOs de request/response
│   │   ├── mapper/           # Mappers entre capas
│   │   └── service/          # Servicios de aplicación
│   ├── domain/               # Entidades y lógica de negocio
│   │   ├── model/            # Modelos de dominio
│   │   └── usecase/          # Casos de uso
│   └── infrastructure/       # Adaptadores externos
│       ├── config/           # Configuraciones
│       ├── controller/       # Controladores REST
│       └── repository/       # Repositorios DynamoDB
└── test/                     # Tests unitarios e integración
```

## 🧪 Testing

### Ejecutar tests
```bash
./gradlew test
```

### Cobertura de código
```bash
./gradlew jacocoTestReport
```


### Docker

1. **Construir imagen**
```bash
docker build -t spacex-launches-api:latest .
```

2. **Subir a ECR**
```bash
docker tag spacex-launches-api:latest <account>.dkr.ecr.us-east-1.amazonaws.com/spacex-launches-api:latest
docker push <account>.dkr.ecr.us-east-1.amazonaws.com/spacex-launches-api:latest
```

```

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## 👨‍💻 Autor

**Cristhian Moreno**
- GitHub: [@cristhianm30](https://github.com/cristhianm30)

