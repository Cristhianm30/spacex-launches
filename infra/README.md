# SpaceX Launches - Infraestructura como Código (IaC)

Este directorio contiene la **Infraestructura como Código (IaC)** para la aplicación *SpaceX Launches*, implementada con **AWS CloudFormation**.  
La infraestructura está organizada en 4 plantillas principales para facilitar el control y la modularidad.

## Arquitectura

- **Frontend**: S3 Static Website + CloudFront CDN
- **Backend**: ECS Fargate + Application Load Balancer
- **API**: API Gateway REST v1
- **CI/CD**: GitHub Actions con OIDC authentication

## Estructura de archivos

```
infra/
├── 01-foundation.yaml       # IAM roles, OIDC provider, ECR backend, security groups
├── 02-compute.yaml          # ECS cluster, definiciones de tareas backend
├── 03-networking.yaml       # ALB, target groups y API Gateway
├── 04-frontend.yaml         # S3 bucket, CloudFront distribution
├── foundation-params.json   # Parámetros para Foundation
├── compute-params.json      # Parámetros para Compute
├── networking-params.json   # Parámetros para Networking
├── frontend-params.json     # Parámetros para Frontend
└── README.md                # Este archivo
```

## Prerrequisitos

1. **AWS CLI** configurado con credenciales y permisos adecuados.  
2. **Docker** instalado y en ejecución.  
3. Una **VPC** y al menos 2 **subnets públicas** creadas previamente.  

Permisos mínimos requeridos en AWS:
- CloudFormation  
- ECR (solo para backend)
- ECS  
- IAM  
- EC2 (para SG y VPC)  
- Elastic Load Balancer  
- API Gateway  
- CloudWatch Logs
- S3
- CloudFront
- Certificate Manager (ACM)  

## Variables necesarias

Antes de empezar, configura los siguientes archivos de parámetros:

**foundation-params.json**
```json
[
  {
    "ParameterKey": "VpcId",
    "ParameterValue": "vpc-12345678"
  },
  {
    "ParameterKey": "ProjectName",
    "ParameterValue": "spacex-launches"
  }
]
```

**networking-params.json**
```json
[
  {
    "ParameterKey": "VpcId",
    "ParameterValue": "vpc-12345678"
  },
  {
    "ParameterKey": "Subnets",
    "ParameterValue": "subnet-12345678,subnet-87654321"
  },
  {
    "ParameterKey": "ProjectName",
    "ParameterValue": "spacex-launches"
  }
]
```

**compute-params.json**
```json
[
  {
    "ParameterKey": "VpcId",
    "ParameterValue": "vpc-12345678"
  },
  {
    "ParameterKey": "Subnets",
    "ParameterValue": "subnet-12345678,subnet-87654321"
  },
  {
    "ParameterKey": "ProjectName",
    "ParameterValue": "spacex-launches"
  },
  {
    "ParameterKey": "BackendImageTag",
    "ParameterValue": "latest"
  }
]
```

**frontend-params.json**
```json
[
  {
    "ParameterKey": "ProjectName",
    "ParameterValue": "spacex-launches"
  }
]
```

## Pasos de despliegue

### 1. Desplegar Foundation (IAM roles, OIDC, ECR backend, SG)

```bash
aws cloudformation create-stack 
  --stack-name spacex-launches-foundation 
  --template-body file://01-foundation.yaml 
  --parameters file://foundation-params.json 
  --capabilities CAPABILITY_NAMED_IAM 
  --region us-east-1
```

✅ Espera a que el estado sea `CREATE_COMPLETE` antes de continuar.

---

### 2. Login en ECR (solo para backend)

```bash
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com
```

---

### 3. Construir y subir imagen Docker del Backend

```bash
cd ../spacex-launches-back

# Build de la imagen
docker build -t <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/spacex-launches-back:latest .

# Push de la imagen
docker push <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/spacex-launches-back:latest

cd ../infra
```

---

### 4. Desplegar Networking (ALB + API Gateway)

```bash
aws cloudformation create-stack 
  --stack-name spacex-launches-networking 
  --template-body file://03-networking.yaml 
  --parameters file://networking-params.json 
  --region us-east-1
```

✅ Espera a `CREATE_COMPLETE`.

---

### 5. Desplegar Compute (ECS backend)

```bash
aws cloudformation create-stack 
  --stack-name spacex-launches-compute 
  --template-body file://02-compute.yaml 
  --parameters file://compute-params.json 
  --region us-east-1
```

✅ Espera a `CREATE_COMPLETE`.

---

### 6. Desplegar Frontend (S3 + CloudFront)

```bash
aws cloudformation create-stack 
  --stack-name spacex-launches-frontend 
  --template-body file://04-frontend.yaml 
  --parameters file://frontend-params.json 
  --region us-east-1
```

✅ Espera a `CREATE_COMPLETE`.

---

### 7. Desplegar Frontend manualmente (primera vez)

**Navegar al directorio del frontend:**
```bash
cd ../spacex-launches-front
```

**Instalar dependencias:**
```bash
npm ci
```

**Build para producción:**
```bash
npm run build -- --configuration production
```

**Subir archivos a S3:**
```bash
aws s3 sync dist/spacex-launches-front/browser/ s3://spacex-launches-frontend-<AWS_ACCOUNT_ID>/ 
  --delete 
  --region us-east-1 
  --profile spacex
```

**Invalidar CloudFront:**
```bash
# Obtener Distribution ID
aws cloudformation describe-stacks --stack-name spacex-launches-frontend --query "Stacks[0].Outputs[?OutputKey=='CloudFrontDistributionId'].OutputValue" --output text --region us-east-1 --profile spacex

# Usar el ID obtenido en el comando de invalidación
aws cloudfront create-invalidation --distribution-id <DISTRIBUTION_ID> --paths "/*" --profile spacex
```

**Volver al directorio infra:**
```bash
cd ../infra
```

---

### 8. Obtener URLs de la aplicación

**Frontend URL (CloudFront):**
```bash
aws cloudformation describe-stacks 
  --stack-name spacex-launches-frontend 
  --region us-east-1 
  --query "Stacks[0].Outputs[?OutputKey=='FrontendURL'].OutputValue" 
  --output text
```

**API Gateway URL:**
```bash
aws cloudformation describe-stacks 
  --stack-name spacex-launches-networking 
  --region us-east-1 
  --query "Stacks[0].Outputs[?OutputKey=='ApiGatewayUrl'].OutputValue" 
  --output text
```

---

## Orden de despliegue

1. **Foundation** → Crea IAM roles, ECR backend y security groups
2. **Networking** → Crea ALB y API Gateway (solo para backend)
3. **Compute** → Crea ECS cluster y servicio backend
4. **Frontend** → Crea S3 bucket y CloudFront distribution

## Deployment automático

Una vez desplegada la infraestructura y realizado el primer deployment manual:

- **Backend**: Push a `develop` → GitHub Actions → Build → Push ECR → Update ECS
- **Frontend**: Push a `develop` → GitHub Actions → Build Angular → Sync S3 → Invalidate CloudFront

💡 **Nota**: El primer deployment del frontend debe hacerse manualmente (paso 7) para tener contenido inicial. Los siguientes deployments serán automáticos con GitHub Actions.

---

### 9. Configurar GitHub Actions Secrets

En tu repositorio GitHub, configura estos secrets:

- `AWS_ACCESS_KEY_ID`: Tu AWS Access Key ID
- `AWS_SECRET_ACCESS_KEY`: Tu AWS Secret Access Key
- `AWS_REGION`: us-east-1
- `AWS_ACCOUNT_ID`: Tu AWS Account ID
- `ECR_REGISTRY`: <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com
- `ECS_CLUSTER`: spacex-launches-cluster
- `ECS_SERVICE_BACK`: spacex-launches-backend-service

---

## Limpieza

Para eliminar la infraestructura, borra los stacks en orden inverso:

```bash
aws cloudformation delete-stack --stack-name spacex-launches-frontend
aws cloudformation delete-stack --stack-name spacex-launches-compute
aws cloudformation delete-stack --stack-name spacex-launches-networking
aws cloudformation delete-stack --stack-name spacex-launches-foundation
```

⚠️ **Importante**: Vacía el bucket S3 antes de eliminar el stack frontend:

```bash
aws s3 rm s3://spacex-launches-frontend-<AWS_ACCOUNT_ID> --recursive
```
