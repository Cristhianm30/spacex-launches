```mermaid
sequenceDiagram
autonumber
participant U as User/Browser
participant CF as CloudFront
participant S3 as S3 (SPA)
participant G as API Gateway (/api)
participant ALB as ALB
participant API as ECS Backend API
participant DDB as DynamoDB
participant EB as EventBridge (6h)
participant GS as API Gateway (/admin/sync)
participant L as Lambda (Data Sync)
participant SX as SpaceX API

rect rgba(240,240,240,0.5)
U->>CF: GET /
CF->>S3: fetch assets
S3-->>CF: 200 (static)
CF-->>U: 200 (SPA)
end

U->>G: GET /api/launches
G->>ALB: forward
ALB->>API: HTTP
API->>DDB: Query launches
DDB-->>API: Items
API-->>U: 200 JSON (via ALB/G)

par Sync programado
  EB->>L: Trigger (cada 6h)
and Sync bajo demanda
  GS->>L: Invoke (/admin/sync)
end
L->>SX: GET launches
SX-->>L: 200 JSON
L->>DDB: Upsert
```

```mermaid
flowchart LR
  %% --- FRONTEND ---
  User[User / Browser]
  subgraph Frontend
    CF[CloudFront (CDN)]
    S3[S3 (Static SPA)]
  end

  User -->|GET /| CF -->|fetch SPA| S3

  %% --- BACKEND ---
  subgraph Backend
    APIGW[API Gateway (/api)]
    ALB[ALB]
    ECS[ECS Fargate (Backend API)]
    DDB[((DynamoDB))]
  end

  User -->|HTTPS /api/*| APIGW --> ALB --> ECS
  ECS <--> DDB

  %% --- DATA SYNC ---
  subgraph "Data Sync"
    EB[EventBridge (cron 6h)]
    APIGW_SYNC[API Gateway (/admin/sync)]
    L[Lambda (Data Sync)]
  end

  SpaceX[[SpaceX API (External)]]

  EB -->|trigger| L
  APIGW_SYNC -->|invoke| L
  L -->|GET launches| SpaceX
  L -->|upsert| DDB

  %% opcional: enriquecimiento directo en tiempo real
  ECS -. optional enrich .-> SpaceX
```


