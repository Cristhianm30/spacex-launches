# 👤 Configuración de un Usuario IAM

## 📌 Objetivo
Crear un **usuario IAM dedicado** con permisos mínimos necesarios para trabajar con la CLI de AWS o herramientas de automatización (ejemplo: SAM, Terraform, Serverless Framework).  

---

## 🛠️ Pasos

### 1. Crear el usuario IAM
1. Ingresar a la consola de AWS → **IAM → Users → Add users**.  
2. Asignar un **nombre descriptivo** al usuario (ejemplo: `tech-user-cli`).  
3. Tipo de acceso:  
   - ✅ **Access key - Programmatic access** (para CLI y herramientas de despliegue).  
   - ❌ Acceso a consola solo si es estrictamente necesario.  

---

### 2. Asignar permisos
Adjuntar las **políticas administradas por AWS** según el caso de uso:  

- `AmazonS3FullAccess` → subir artefactos a S3.  
- `AWSCloudFormationFullAccess` → crear y administrar stacks.  
- `AWSLambda_FullAccess` → gestionar funciones Lambda.  
- `AmazonDynamoDBFullAccess` → acceso a tablas DynamoDB.  
- `AmazonAPIGatewayAdministrator` → crear y administrar API Gateway.  
- `CloudWatchLogsFullAccess` → registrar y consultar logs.  
- `AmazonEventBridgeFullAccess` → programar ejecuciones con EventBridge.  
- `IAMFullAccess` → **solo si** la herramienta necesita crear roles automáticamente.  

👉 Estos permisos dependen del **alcance del proyecto**.  
  

---

### 3. Guardar credenciales
Al crear el usuario se entregan un **Access Key ID** y un **Secret Access Key**.  
Guárdalos en el archivo `~/.aws/credentials` usando un perfil dedicado:  

```ini
[tech-profile]
aws_access_key_id = <ACCESS_KEY_ID>
aws_secret_access_key = <SECRET_ACCESS_KEY>
region = us-east-1
```

---

### 4. Configurar AWS CLI

Ejecutar el siguiente comando:

```bash
aws configure --profile tech-profile
```

---

### 5. Usar el perfil en la CLI o herramientas
Ejemplos:  

```bash
# Desplegar con SAM, Terraform o Serverless
sam deploy --guided --profile tech-profile
terraform apply -var-file=vars.tfvars -profile=tech-profile
sls deploy --aws-profile tech-profile

# Consultar recursos desde la CLI
aws s3 ls --profile tech-profile
aws logs tail /aws/lambda/my-function --follow --profile tech-profile
```
