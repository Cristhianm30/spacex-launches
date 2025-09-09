# 🚀 CI/CD con GitHub Actions - SpaceX Launches

Este repositorio implementa **pipelines automatizados con GitHub Actions** para desplegar los tres componentes de la solución:

- **Frontend (Angular 20)** → desplegado en Amazon S3 + CloudFront CDN.  
- **Backend (Spring Boot + Gradle + Java 21)** → desplegado en Amazon ECS Fargate.  
- **Lambda (Python)** → función `spacex-launches-sync`, encargada de sincronizar datos desde la API pública de SpaceX hacia DynamoDB.

---

## 📌 Estructura de Workflows

Los archivos de workflows están en `.github/workflows/`:

- `frontend.yml` → CI/CD para el **frontend Angular**.
- `backend.yml` → CI/CD para el **backend Spring Boot**.
- `lambda.yml` → CI/CD para la **Lambda Python**.

Cada workflow está configurado para ejecutarse **solo cuando haya cambios en su carpeta correspondiente** o cuando se actualiza el propio archivo del workflow.

---

## 🔹 Frontend (`frontend.yml`)

- **Trigger**: cambios en `spacex-launches-front/**`  
- **Pasos principales**:
  1. Instala Node.js 20 y dependencias con `npm ci`.
  2. Ejecuta pruebas (`npm test` con Jest).
  3. Construye la aplicación Angular para producción (`npm run build --configuration production`).
  4. Sincroniza los archivos estáticos con el bucket S3.
  5. Invalida la caché de CloudFront para reflejar los cambios inmediatamente.

**Autenticación**: Utiliza **OIDC (OpenID Connect)** para autenticarse con AWS sin necesidad de credenciales estáticas.  
**Deployment**: Los archivos se suben desde `dist/spacex-launches-front/browser/` al bucket S3 configurado.

---

## 🔹 Backend (`backend.yml`)

- **Trigger**: cambios en `spacex-launches-back/**`  
- **Pasos principales**:
  1. Instala JDK 21 (Temurin).
  2. Valida y ejecuta Gradle Wrapper (`./gradlew`).
  3. Corre pruebas unitarias y genera reportes con JaCoCo.
  4. Sube los reportes de tests y cobertura como artifacts.
  5. Construye la imagen Docker y la sube a Amazon ECR (tag `latest`).
  6. Redeploy en ECS con `aws ecs update-service`.

**Autenticación**: Utiliza credenciales AWS tradicionales (Access Key + Secret Key).  
El contenedor del backend se publica con `:latest` y se fuerza un redeploy en ECS Fargate.

---

## 🔹 Lambda (`lambda.yml`)

- **Trigger**: cambios en `spacex-launches-lambda/**`  
- **Pasos principales**:
  1. Instala Python 3.12 y dependencias de `requirements.txt`.
  2. Corre pruebas unitarias con `pytest`.
  3. Empaqueta el código de `src/` y dependencias en `function.zip`.
  4. Actualiza la función **spacex-launches-sync** con `aws lambda update-function-code`.

El handler se mantiene como `app.lambda_handler`, por lo cual el `workflow` asegura que `app.py` quede en la raíz del zip junto con las dependencias.

---

## 🔐 Secrets requeridos

Para que los workflows funcionen, se deben configurar estos **GitHub Secrets**:

### Para Backend y Lambda (credenciales tradicionales):
- `AWS_ACCESS_KEY_ID` → credenciales IAM con permisos para ECR, ECS y Lambda.
- `AWS_SECRET_ACCESS_KEY`
- `AWS_REGION` → ej. `us-east-1`
- `AWS_ACCOUNT_ID` → ID de la cuenta AWS

### Para Frontend (OIDC - sin credenciales estáticas):
- No requiere secrets adicionales, usa **OIDC** automáticamente

### Recursos AWS:
- `ECR_REGISTRY` → ej. `123456789.dkr.ecr.us-east-1.amazonaws.com`
- `ECS_CLUSTER` → nombre del cluster ECS (ej. `spacex-launches-cluster`)
- `ECS_SERVICE_BACK` → nombre del Service ECS del backend (ej. `spacex-launches-backend-service`)
- `LAMBDA_FUNCTION_NAME` → nombre de la función Lambda (ej. `spacex-launches-sync`)

---

## ✅ Resumen de despliegues

- **Frontend** → Angular → Build estático → S3 + CloudFront (CDN).  
- **Backend** → Spring Boot → Docker → ECR → ECS (Fargate).  
- **Lambda** → Python → ZIP + dependencias → AWS Lambda.  

Cada push a `main` en la carpeta correspondiente dispara el pipeline y actualiza el servicio o función automáticamente.

### 🏗️ Arquitectura de Deployment:

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Frontend      │    │     Backend      │    │     Lambda      │
│                 │    │                  │    │                 │
│ Angular Build   │    │ Spring Boot JAR  │    │ Python Package  │
│       ↓         │    │       ↓          │    │       ↓         │
│ S3 Static Host  │    │ Docker Image     │    │ ZIP + deps      │
│       ↓         │    │       ↓          │    │       ↓         │
│ CloudFront CDN  │    │ ECR Registry     │    │ AWS Lambda      │
│                 │    │       ↓          │    │                 │
│ OIDC Auth       │    │ ECS Fargate      │    │ EventBridge     │
└─────────────────┘    └──────────────────┘    └─────────────────┘
```


---

🚀 Con esto se logra un flujo de **CI/CD completo** para los tres componentes de la prueba técnica, garantizando calidad (tests), integración y despliegue automático en AWS.
