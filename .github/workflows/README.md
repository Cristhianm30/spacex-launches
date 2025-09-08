# 🚀 CI/CD con GitHub Actions - SpaceX Launches

Este repositorio implementa **pipelines automatizados con GitHub Actions** para desplegar los tres componentes de la solución:

- **Frontend (Angular 20)** → desplegado en Amazon ECS Fargate.  
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
  3. Construye la imagen Docker y la sube a Amazon ECR.
  4. Ejecuta `aws ecs update-service` para redeploy en ECS Fargate.

El contenedor del frontend se publica en ECR con el tag `latest`.  
El ECS Service usa la task definition con `:latest` y se fuerza un redeploy en cada pipeline.

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

El contenedor del backend también se publica con `:latest` y se fuerza un redeploy.

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

- `AWS_ACCESS_KEY_ID` → credenciales IAM con permisos para ECR, ECS y Lambda.
- `AWS_SECRET_ACCESS_KEY`
- `AWS_REGION` → ej. `us-east-1`
- `ECR_REGISTRY` → ej. `944688893944.dkr.ecr.us-east-1.amazonaws.com`
- `ECS_CLUSTER` → nombre del cluster ECS (ej. `spacex-launches-cluster`)
- `ECS_SERVICE_FRONT` → nombre del Service ECS del frontend (ej. `spacex-launches-frontend-service`)
- `ECS_SERVICE_BACK` → nombre del Service ECS del backend (ej. `spacex-launches-backend-service`)
- `LAMBDA_FUNCTION_NAME` → nombre de la función Lambda (ej. `spacex-launches-sync`)

---

## ✅ Resumen de despliegues

- **Frontend** → Angular → Docker → ECR → ECS (Fargate).  
- **Backend** → Spring Boot → Docker → ECR → ECS (Fargate).  
- **Lambda** → Python → ZIP + dependencias → AWS Lambda.  

Cada push a `main` o `develop` en la carpeta correspondiente dispara el pipeline y actualiza el servicio o función automáticamente.

---

## 📖 Referencias útiles

- [GitHub Actions Docs](https://docs.github.com/en/actions)
- [Amazon ECS Developer Guide](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/Welcome.html)
- [Amazon Lambda Docs](https://docs.aws.amazon.com/lambda/latest/dg/welcome.html)
- [Amazon ECR Docs](https://docs.aws.amazon.com/AmazonECR/latest/userguide/what-is-ecr.html)

---

🚀 Con esto se logra un flujo de **CI/CD completo** para los tres componentes de la prueba técnica, garantizando calidad (tests), integración y despliegue automático en AWS.
