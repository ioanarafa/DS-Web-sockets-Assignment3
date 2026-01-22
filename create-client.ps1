# Script pentru crearea unui user normal (CLIENT)
# Rulează acest script pentru a crea un user de tip CLIENT

$API_URL = "http://localhost/auth/register"

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "Creating CLIENT User" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

$body = @{
    username = "client"
    password = "client"
    role = "CLIENT"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri $API_URL -Method POST -Body $body -ContentType "application/json"
    Write-Host "✅ Client user created successfully!" -ForegroundColor Green
    Write-Host "Username: client" -ForegroundColor Yellow
    Write-Host "Password: client" -ForegroundColor Yellow
    Write-Host "Role: CLIENT" -ForegroundColor Yellow
} catch {
    if ($_.Exception.Response.StatusCode -eq 400) {
        Write-Host "⚠️  User 'client' already exists" -ForegroundColor Yellow
        Write-Host "Trying to create with different username..." -ForegroundColor Yellow
        
        $body = @{
            username = "user1"
            password = "user1"
            role = "CLIENT"
        } | ConvertTo-Json
        
        try {
            $response = Invoke-RestMethod -Uri $API_URL -Method POST -Body $body -ContentType "application/json"
            Write-Host "✅ Client user created successfully!" -ForegroundColor Green
            Write-Host "Username: user1" -ForegroundColor Yellow
            Write-Host "Password: user1" -ForegroundColor Yellow
            Write-Host "Role: CLIENT" -ForegroundColor Yellow
        } catch {
            Write-Host "❌ Error: $_" -ForegroundColor Red
        }
    } else {
        Write-Host "❌ Error: $_" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "Now you can login with:" -ForegroundColor Cyan
Write-Host "  Username: client (or user1)" -ForegroundColor White
Write-Host "  Password: client (or user1)" -ForegroundColor White
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "As a CLIENT user you will see:" -ForegroundColor Green
Write-Host "  - Your assigned devices only" -ForegroundColor White
Write-Host "  - Energy consumption monitoring" -ForegroundColor White
Write-Host "  - Overconsumption alerts" -ForegroundColor White
Write-Host "  - Support chat" -ForegroundColor White
