#!/bin/bash

# Quick Start Script for Factory Project

set -e

echo "==========================================="
echo "Factory Production Management System"
echo "Quick Start Script"
echo "==========================================="

# Check Docker installation
if ! command -v docker &> /dev/null; then
    echo "ERROR: Docker is not installed. Please install Docker first."
    echo "Visit: https://www.docker.com/products/docker-desktop"
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo "ERROR: Docker Compose is not installed. Please install Docker Compose first."
    echo "Visit: https://docs.docker.com/compose/install/"
    exit 1
fi

echo "✓ Docker is installed"
echo "✓ Docker Compose is installed"

# Build and start services
echo ""
echo "Building and starting services..."
docker-compose up --build -d

# Wait for services to be ready
echo ""
echo "Waiting for services to be ready..."
sleep 10

# Check if services are running
if ! docker ps | grep -q "factory-db"; then
    echo "ERROR: Database failed to start"
    docker-compose logs db
    exit 1
fi

if ! docker ps | grep -q "factory-api"; then
    echo "ERROR: Backend API failed to start"
    docker-compose logs backend
    exit 1
fi

if ! docker ps | grep -q "factory-ui"; then
    echo "ERROR: Frontend failed to start"
    docker-compose logs frontend
    exit 1
fi

echo "✓ All services started successfully!"
echo ""
echo "==========================================="
echo "Services are now running:"
echo "==========================================="
echo ""
echo "Frontend:  http://localhost:3000"
echo "Backend:   http://localhost:8080"
echo "Database:  localhost:5432"
echo ""
echo "==========================================="
echo "Documentation:"
echo "==========================================="
echo ""
echo "README:               README.md"
echo "API Examples:         API_EXAMPLES.md"
echo "Sample Data:          SAMPLE_DATA.md"
echo "Deployment:           DEPLOYMENT.md"
echo "Architecture:         ARCHITECTURE.md"
echo "Contributing:         CONTRIBUTING.md"
echo "GitHub Setup:         GITHUB_SETUP.md"
echo ""
echo "==========================================="
echo "Useful Commands:"
echo "==========================================="
echo ""
echo "View logs:            docker-compose logs -f"
echo "Stop services:        docker-compose down"
echo "Stop and remove data: docker-compose down -v"
echo "Rebuild services:     docker-compose up --build"
echo ""
echo "Open frontend in browser: http://localhost:3000"
echo ""
echo "==========================================="
