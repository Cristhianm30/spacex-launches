# 👤 Configuración del usuario IAM para `spacex-tech-test`

## 📌 Objetivo
Crear un **usuario IAM dedicado** para la prueba técnica con permisos mínimos necesarios para desplegar y operar la solución con **AWS SAM CLI**.  

---

## 🛠️ Pasos

### 1. Crear el usuario IAM
1. Ingresar a la consola de AWS → **IAM → Users → Add users**.  
2. Nombre: `spacex-tech-test`.  
3. Tipo de acceso:  
   - ✅ **Access key - Programmatic access** (para CLI y SAM).  
   - ❌ No asignar acceso a la consola (opcional).  

---

### 2. Asignar permisos
Adjuntar las siguientes **políticas administradas por AWS**:  

- `AmazonS3FullAccess` → SAM sube artefactos a un bucket S3.  
- `AWSCloudFormationFullAccess` → SAM despliega recursos con CloudFormation.  
- `AWSLambda_FullAccess` → desplegar y administrar funciones Lambda.  
- `AmazonDynamoDBFullAccess` → acceso a la tabla DynamoDB.  
- `AmazonAPIGatewayAdministrator` → crear y administrar API Gateway.  
- `CloudWatchLogsFullAccess` → escribir y consultar logs en CloudWatch.  
- `AmazonEventBridgeFullAccess` → crear reglas de ejecución programada.  
- `IAMFullAccess` → solo si necesitas crear roles de ejecución automáticamente desde SAM. 

👉 Estos permisos son **suficientes y necesarios** para la prueba técnica.  
👉 En producción se recomendaría usar permisos más restrictivos, pero aquí es correcto.  

---

### 3. Guardar credenciales
Al crear el usuario, AWS dará un **Access Key ID** y un **Secret Access Key**.  
Guárdalos en el archivo `~/.aws/credentials` usando un perfil dedicado:  

```ini
[spacex]
aws_access_key_id = <ACCESS_KEY_ID>
aws_secret_access_key = <SECRET_ACCESS_KEY>
region = us-east-1
```

---
### 4. Configurar AWS CLI

Ejecutar el siguiente comando:

```bash
aws configure --profile spacex
```
---

### 5. Usar el perfil en SAM y AWS CLI
Ejemplo en la Lambda:
```
sam build
sam deploy --guided --profile spacex
aws dynamodb scan --table-name spacex-launches --profile spacex
aws logs tail /aws/lambda/spacex-launches-sync --follow --profile spacex

Uso de --profile en CLI/SAM para mayor seguridad y claridad.
```