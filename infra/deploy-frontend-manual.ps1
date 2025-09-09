# Script para desplegar manualmente el frontend a S3 antes del primer pipeline
# Ejecutar desde el directorio raíz del proyecto

param(
    [Parameter(Mandatory=$true)]
    [string]$AwsAccountId,
    
    [Parameter(Mandatory=$false)]
    [string]$Region = "us-east-1"
)

Write-Host "🚀 Desplegando frontend manualmente a S3..." -ForegroundColor Green

# 1. Navegar al directorio del frontend
Set-Location "spacex-launches-front"

# 2. Instalar dependencias
Write-Host "📦 Instalando dependencias..." -ForegroundColor Yellow
npm ci

# 3. Build para producción
Write-Host "🔨 Building Angular app..." -ForegroundColor Yellow
npm run build -- --configuration production

# 4. Verificar que el build existe
if (-not (Test-Path "dist/spacex-launches-front")) {
    Write-Error "❌ Build failed - directorio dist no encontrado"
    exit 1
}

# 5. Sync a S3
$bucketName = "spacex-launches-frontend-$AwsAccountId"
Write-Host "☁️ Sincronizando archivos a S3: $bucketName" -ForegroundColor Yellow

# Subir archivos estáticos con cache largo
aws s3 sync dist/spacex-launches-front/ s3://$bucketName/ `
    --delete `
    --cache-control "public,max-age=31536000,immutable" `
    --exclude "*.html" `
    --exclude "*.json" `
    --region $Region

# Subir HTML y JSON sin cache
aws s3 sync dist/spacex-launches-front/ s3://$bucketName/ `
    --delete `
    --cache-control "public,max-age=0,must-revalidate" `
    --include "*.html" `
    --include "*.json" `
    --region $Region

# 6. Invalidar CloudFront
Write-Host "🔄 Invalidando cache de CloudFront..." -ForegroundColor Yellow
$distributionId = aws cloudformation describe-stacks `
    --stack-name spacex-launches-frontend `
    --query "Stacks[0].Outputs[?OutputKey=='CloudFrontDistributionId'].OutputValue" `
    --output text `
    --region $Region

if ($distributionId -and $distributionId -ne "None") {
    aws cloudfront create-invalidation `
        --distribution-id $distributionId `
        --paths "/*" `
        --region $Region
    
    Write-Host "✅ Invalidación de CloudFront iniciada" -ForegroundColor Green
} else {
    Write-Warning "⚠️ No se pudo obtener el Distribution ID de CloudFront"
}

# 7. Obtener URL del frontend
$frontendUrl = aws cloudformation describe-stacks `
    --stack-name spacex-launches-frontend `
    --query "Stacks[0].Outputs[?OutputKey=='FrontendURL'].OutputValue" `
    --output text `
    --region $Region

Write-Host ""
Write-Host "🎉 Frontend desplegado exitosamente!" -ForegroundColor Green
Write-Host "🌐 URL: $frontendUrl" -ForegroundColor Cyan
Write-Host "⏰ Deployment completado: $(Get-Date)" -ForegroundColor Gray

# Volver al directorio original
Set-Location ".."
