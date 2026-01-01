# RAG Service Startup Script
Write-Host "Starting RAG Service..." -ForegroundColor Cyan

# Activate virtual environment
& "E:\Project Practice\Write\python-rag-env\Scripts\Activate.ps1"

# Change to core-code-and-deps directory
Set-Location "E:\Project Practice\Write\core-code-and-deps"

# Start service
Write-Host "Virtual environment activated" -ForegroundColor Green
Write-Host "Starting RAG service on port 8001..." -ForegroundColor Yellow

python rag_service.py
