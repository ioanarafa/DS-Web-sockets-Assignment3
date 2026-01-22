# Script pentru crearea unui user admin
# Rulează acest script pentru a crea un user admin

$API_URL = "http://localhost/auth/register"

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "Creating Admin User" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

$body = @{
    username = "admin"
    password = "admin"
    role = "ADMIN"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri $API_URL -Method POST -Body $body -ContentType "application/json"
    Write-Host "✅ Admin user created successfully!" -ForegroundColor Green
    Write-Host "Username: admin" -ForegroundColor Yellow
    Write-Host "Password: admin" -ForegroundColor Yellow
    Write-Host "Role: ADMIN" -ForegroundColor Yellow
} catch {
    if ($_.Exception.Response.StatusCode -eq 400) {
        Write-Host "⚠️  User already exists or invalid request" -ForegroundColor Yellow
        Write-Host "Trying to create with different username..." -ForegroundColor Yellow
        
        $body = @{
            username = "admin2"
            password = "admin"
            role = "ADMIN"
        } | ConvertTo-Json
        
        try {
            $response = Invoke-RestMethod -Uri $API_URL -Method POST -Body $body -ContentType "application/json"
            Write-Host "✅ Admin user created successfully!" -ForegroundColor Green
            Write-Host "Username: admin2" -ForegroundColor Yellow
            Write-Host "Password: admin" -ForegroundColor Yellow
            Write-Host "Role: ADMIN" -ForegroundColor Yellow
        } catch {
            Write-Host "❌ Error: $_" -ForegroundColor Red
        }
    } else {
        Write-Host "❌ Error: $_" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "Now you can login with:" -ForegroundColor Cyan
Write-Host "  Username: admin (or admin2)" -ForegroundColor White
Write-Host "  Password: admin" -ForegroundColor White
