param(
  [string]$EnvPrefix = ""
)

$ErrorActionPreference = "Stop"

if (-not $EnvPrefix -or $EnvPrefix -eq "") {
  $EnvPrefix = Join-Path $PSScriptRoot "conda-env"
}

Write-Host "Using conda prefix: $EnvPrefix" -ForegroundColor Cyan

# Decide shell executable (prefer pwsh if available, else Windows PowerShell)
$pwshCmd = Get-Command pwsh -ErrorAction SilentlyContinue
if ($pwshCmd) {
  $ShellExe = 'pwsh'
} else {
  $ShellExe = Join-Path $env:SystemRoot 'System32\WindowsPowerShell\v1.0\powershell.exe'
}
Write-Host "Using shell: $ShellExe" -ForegroundColor Cyan

# Helper to (re)start a background job safely
function Start-Or-RestartJob {
  param(
    [string]$Name,
    [scriptblock]$Script
  )
  $job = Get-Job -Name $Name -ErrorAction SilentlyContinue
  if ($job) {
    try { Stop-Job -Job $job -ErrorAction SilentlyContinue } catch {}
    try { Remove-Job -Job $job -ErrorAction SilentlyContinue } catch {}
  }
  Start-Job -Name $Name -ScriptBlock $Script | Out-Null
}

# Backend
Start-Or-RestartJob -Name "aiw-backend" -Script {
  conda run -p "$using:EnvPrefix" $using:ShellExe -NoLogo -NoProfile -Command '
    $env:JAVA_HOME = "$env:CONDA_PREFIX\Library";
    Set-Location "$($using:PSScriptRoot)\backend";
    .\mvnw.cmd spring-boot:run -DskipTests'
}

# Frontend
Start-Or-RestartJob -Name "aiw-frontend" -Script {
  conda run -p "$using:EnvPrefix" $using:ShellExe -NoLogo -NoProfile -Command '
    Set-Location "$($using:PSScriptRoot)\frontend";
    npm run dev -- --host 0.0.0.0'
}

Write-Host "Started jobs:" -ForegroundColor Green
Get-Job -Name aiw-* | Format-Table -AutoSize Id, Name, State, HasMoreData
