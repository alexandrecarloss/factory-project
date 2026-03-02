@echo off
REM Quick Start Script for Factory Project (Windows)

echo ==========================================
echo Factory Production Management System
echo Quick Start Script (Windows)
echo ==========================================

REM Check Docker installation
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Docker is not installed. Please install Docker first.
    echo Visit: https://www.docker.com/products/docker-desktop
    exit /b 1
)

docker-compose --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Docker Compose is not installed. Please install Docker Compose first.
    echo Visit: https://docs.docker.com/compose/install/
    exit /b 1
)

echo [OK] Docker is installed
echo [OK] Docker Compose is installed

echo.
echo Building and starting services...
docker-compose up --build -d

echo.
echo Waiting for services to be ready...
timeout /t 10 /nobreak

echo.
echo ==========================================
echo Services are now running:
echo ==========================================
echo.
echo Frontend:  http://localhost:3000
echo Backend:   http://localhost:8080
echo Database:  localhost:5432
echo.
echo ==========================================
echo Documentation:
echo ==========================================
echo.
echo README.md             - Main documentation
echo API_EXAMPLES.md       - API usage examples
echo SAMPLE_DATA.md        - Sample data for testing
echo DEPLOYMENT.md         - Deployment instructions
echo ARCHITECTURE.md       - Architecture and design decisions
echo CONTRIBUTING.md       - Contributing guidelines
echo GITHUB_SETUP.md       - GitHub repository setup
echo.
echo ==========================================
echo Useful Commands:
echo ==========================================
echo.
echo View logs:            docker-compose logs -f
echo Stop services:        docker-compose down
echo Stop and remove data: docker-compose down -v
echo.
echo Opening frontend in browser: http://localhost:3000
start http://localhost:3000
