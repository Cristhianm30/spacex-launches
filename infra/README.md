# SpaceX Launches - Infraestructura como Código (IaC)

Este directorio contiene la **Infraestructura como Código (IaC)** para la aplicación *SpaceX Launches*, implementada con **AWS CloudFormation**.  
La infraestructura está organizada en 3 plantillas principales para facilitar el control y la modularidad.

## Estructura de archivos

```
infra/
├── 01-foundation.yaml       # IAM roles, repositorios ECR, security groups
├── 02-compute.yaml          # ECS cluster, definiciones de tareas y servicios
├── 03-networking.yaml       # ALB, target groups y API Gateway
├── foundation-params.json   # Parámetros de Foundation
├── compute-params.json      # Parámetros de Compute
├── networking-params.json   # Parámetros de Networking
└── README.md                # Este archivo
```

## Prerrequisitos

1. **AWS CLI** configurado con credenciales y permisos adecuados.  
2. **Docker** instalado y en ejecución.  
3. Una **VPC** y al menos 2 **subnets públicas** creadas previamente.  

Permisos mínimos requeridos en AWS:
- CloudFormation  
- ECR  
- ECS  
- IAM  
- EC2 (para SG y VPC)  
- Elastic Load Balancer  
- API Gateway  
- CloudWatch Logs  

## Variables necesarias

Antes de empezar, define los siguientes valores en los diferentes archivos -params.json de la carpeta:

```
AWS Account ID:  [Ej: 944688893944]
VPC ID:          [Ej: REMOVED]
Subnet 1:        [Ej: REMOVED]
Subnet 2:        [Ej: REMOVED]
Region:          us-east-1
```

## Pasos de despliegue

### 1. Desplegar Foundation (repositorios ECR, IAM, SG)

```bash
aws cloudformation create-stack   --stack-name spacex-launches-foundation   --template-body file://01-foundation.yaml   --parameters file://foundation-params.json   --capabilities CAPABILITY_NAMED_IAM   --region us-east-1
```

✅ Espera a que el estado sea `CREATE_COMPLETE` antes de continuar.

---

### 2. Login en ECR

```bash
aws ecr get-login-password --region us-east-1   | docker login --username AWS --password-stdin <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com
```

---

### 3. Construir y subir imágenes Docker

**Backend:**
```bash
cd ../spacex-launches-back
docker build -t <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/spacex-launches-back:latest .
docker push <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/spacex-launches-back:latest
```

**Frontend:**
```bash
cd ../spacex-launches-front
docker build -t <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/spacex-launches-front:latest .
docker push <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/spacex-launches-front:latest
cd ../infra
```

---

### 4. Desplegar Networking (ALB + API Gateway)

```bash
aws cloudformation create-stack   --stack-name spacex-launches-networking   --template-body file://03-networking.yaml   --parameters file://networking-params.json   --region us-east-1
```

✅ Espera a `CREATE_COMPLETE`.

---

### 5. Desplegar Compute (ECS + servicios)

```bash
aws cloudformation create-stack   --stack-name spacex-launches-compute   --template-body file://02-compute.yaml   --parameters file://compute-params.json   --region us-east-1
```

✅ Espera a `CREATE_COMPLETE`.

---

### 6. Obtener endpoints de despliegue

**API Gateway:**
```bash
aws cloudformation describe-stacks   --stack-name spacex-launches-networking   --region us-east-1   --query "Stacks[0].Outputs[?OutputKey=='ApiGatewayEndpoint'].OutputValue"   --output text
```

**Load Balancer:**
```bash
aws cloudformation describe-stacks   --stack-name spacex-launches-networking   --region us-east-1   --query "Stacks[0].Outputs[?OutputKey=='ApplicationLoadBalancerDnsName'].OutputValue"   --output text
```

---

## Orden de despliegue

1. **Foundation** → Crea roles, repositorios y security groups.  
2. **Networking** → Crea ALB y API Gateway.  
3. **Compute** → Crea ECS y servicios.  

---

## Limpieza

Para eliminar la infraestructura, borra los stacks en orden inverso:

```bash
aws cloudformation delete-stack --stack-name spacex-launches-compute
aws cloudformation delete-stack --stack-name spacex-launches-networking
aws cloudformation delete-stack --stack-name spacex-launches-foundation
```
