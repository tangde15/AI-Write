$ErrorActionPreference = "SilentlyContinue"

Get-Job -Name aiw-backend, aiw-frontend | Stop-Job
Get-Job -Name aiw-backend, aiw-frontend | Remove-Job

Write-Host "Stopped jobs (if existed): aiw-backend, aiw-frontend" -ForegroundColor Yellow
