# Documentación Lambda SpaceX Launches

## 📌 Descripción

Lambda que consume la **API pública de SpaceX** y sincroniza información de lanzamientos en **DynamoDB**.  
La función puede ejecutarse manualmente mediante **API Gateway** o automáticamente cada **6 horas** con **EventBridge**.

---

## 📂 Estructura del proyecto
```
lambda/
 ├── src/
 │   ├── app.py           # Código principal Lambda
 │   └── __init__.py
 ├── tests/               # Unit e integration tests
 │   ├── unit/
 │   │   └── test_app_with_real_data.py
 │   └── integration/
 │       └── test_lambda_integration.py
 ├── requirements.txt     # Dependencias runtime
 ├── template.yaml        # Infraestructura SAM
 ├── samconfig.toml       # Configuración de despliegue
 └── .gitignore
```

---

## ⚙️ Infraestructura (SAM)

### Recursos

- **DynamoDB**: tabla `spacex-launches` con PK = `launch_id`.
    
- **Lambda**: función `spacex-launches-sync`.
    
- **API Gateway**: endpoint público `/sync`.
    
- **EventBridge**: trigger cada 6 horas.
    

### Deploy

`sam build sam deploy `

Si es la primera vez:

`sam deploy --guided `

---

## 🌍 Variables de entorno

Definidas en `template.yaml`:

|Variable|Default|Descripción|
|---|---|---|
|`SPACEX_API_URL`|`https://api.spacexdata.com/v4/launches`|Endpoint de la API SpaceX|
|`SPACEX_AWS_REGION`|`us-east-1`|Región AWS para recursos|
|`SPACEX_TABLE_NAME`|`spacex-launches`|Nombre de la tabla DynamoDB|
|`SPACEX_REQUEST_TIMEOUT`|`30`|Timeout en segundos para requests|
|`LOG_LEVEL`|`INFO`|Nivel de logging (`INFO`, `DEBUG`, …)|

---

## 🧪 Testing

### Ejecutar tests

Desde `lambda/`:

`pytest -v tests`

### Cobertura actual

- **Unit tests**:
    
    - Validan parsing de datos y upsert en DynamoDB (mockeado).
        
- **Integration tests**:
    
    - Simulan invocación del handler con un evento API Gateway.
        
    - Validan tanto casos exitosos como errores de API.
        

---

## 📜 Logging

Niveles aplicados:

- `INFO` → inicio y fin de la ejecución, resumen de procesados.
    
- `WARNING` → cuando la API no devuelve datos.
    
- `ERROR` → errores de DynamoDB o API externa.
    
- `CRITICAL` → errores inesperados en la Lambda.
    
- `DEBUG` → solo visible si se activa `LOG_LEVEL=DEBUG`.
    

👉 Esto asegura que en **CloudWatch** solo se ve lo esencial, y el debug queda para desarrollo.

---

## 🚀 Validación en AWS

1. **Invocar API manualmente**
    
    `curl https://<api-id>.execute-api.us-east-1.amazonaws.com/Prod/sync`
    
2. **Revisar datos en DynamoDB**
    
    `aws dynamodb scan --table-name spacex-launches --profile spacex`
    
3. **Ver logs en CloudWatch**
    
    `aws logs tail /aws/lambda/spacex-launches-sync --follow --profile spacex`
    

---

## ✅ Buenas prácticas aplicadas

- IaC con SAM (infra reproducible).
    
- Configuración vía variables de entorno (no hardcode).
    
- Logging estructurado con niveles de severidad.
    
- Manejo de errores robusto.
    
- Separación de responsabilidades en funciones (`fetch`, `upsert`, `process`).
    
- Pruebas unitarias e integración con mocks.