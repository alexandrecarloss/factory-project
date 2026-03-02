# Deployment Guide

## Local Development

### Prerequisites

- Docker and Docker Compose
- Java 21 (if running without Docker)
- Node.js 18+ (if running without Docker)
- Maven 3.6+ (if running without Docker)

### Quick Start with Docker Compose

```bash
# Clone and navigate to project
git clone <repository-url>
cd factory-project

# Start all services
docker-compose up --build

# Services will be available at:
# Frontend: http://localhost:3000
# Backend API: http://localhost:8080
# Database: localhost:5432
```

### Manual Development Setup

```bash
# Terminal 1: Start Database
docker-compose up db

# Terminal 2: Start Backend
cd backend/api
mvn clean install
mvn spring-boot:run

# Terminal 3: Start Frontend
cd frontend
npm install
npm run dev
```

## Production Deployment

### Prerequisites for Production

- Docker Engine 20.10+
- Docker Compose 2.0+
- Environment variables configured
- SSL/TLS certificate (recommended)

### Environment Variables

#### Backend (.env)

```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/factory_db
SPRING_DATASOURCE_USERNAME=user
SPRING_DATASOURCE_PASSWORD=securepassword
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
```

#### Frontend (.env.production)

```
VITE_API_URL=https://api.yourdomain.com
VITE_APP_TITLE=Factory Production Management
```

### Deploy with Docker Compose

1. **Update Configuration**

```bash
# Edit docker-compose.yml and environment variables
cp .env.example .env
# Edit .env with production values
```

2. **Build and Start**

```bash
docker-compose -f docker-compose.yml up -d
```

3. **Verify Services**

```bash
docker-compose ps
docker container logs factory-api
docker container logs factory-ui
```

### Deploy on Kubernetes

1. **Create Namespace**

```bash
kubectl create namespace factory
```

2. **Create ConfigMaps and Secrets**

```bash
kubectl create configmap factory-config --from-file=.env -n factory
kubectl create secret generic factory-db-secret --from-literal=password=securepassword -n factory
```

3. **Apply Kubernetes Manifests**

```bash
# Create deployment manifests
kubectl apply -f k8s/ -n factory
```

### Deploy on AWS

#### Using ECS

1. Push images to ECR

```bash
aws ecr create-repository --repository-name factory-api
aws ecr create-repository --repository-name factory-ui

# Build and push
docker build -t factory-api backend/api/
docker tag factory-api:latest <aws-account>.dkr.ecr.<region>.amazonaws.com/factory-api:latest
docker push <aws-account>.dkr.ecr.<region>.amazonaws.com/factory-api:latest
```

2. Create ECS Cluster and Services
3. Configure RDS for PostgreSQL
4. Set up Load Balancer

#### Using Elastic Beanstalk

```bash
# Initialize Elastic Beanstalk
eb init -p docker factory-production

# Create environment
eb create factory-prod

# Deploy
eb deploy
```

### Deploy on Heroku

```bash
# Login to Heroku
heroku login

# Create app
heroku create factory-app

# Add PostgreSQL addon
heroku addons:create heroku-postgresql:standard-0

# Deploy
git push heroku main

# View logs
heroku logs --tail
```

## Monitoring and Logs

### Docker Logs

```bash
# View backend logs
docker logs factory-api

# View frontend logs
docker logs factory-ui

# View database logs
docker logs factory-db

# Tail logs
docker logs -f factory-api
```

### Health Checks

```bash
# Check backend health
curl http://localhost:8080/actuator/health

# Check frontend
curl http://localhost:3000
```

## Backup and Recovery

### Database Backup

```bash
# Create backup
docker exec factory-db pg_dump -U user factory_db > backup.sql

# Restore backup
docker exec -i factory-db psql -U user factory_db < backup.sql
```

### Volume Backup

```bash
# Backup postgres_data volume
docker run --rm -v factory-project_postgres_data:/data -v $(pwd):/backup \
  alpine tar czf /backup/db-backup.tar.gz -C /data .
```

## Troubleshooting

### Backend not connecting to database

1. Check PostgreSQL container is running: `docker ps`
2. Verify connection string in application.properties
3. Check database user permissions
4. Verify network connectivity: `docker network inspect factory-project_default`

### Frontend API calls failing

1. Check CORS configuration in backend
2. Verify API_URL environment variable
3. Check browser console for errors
4. Test API directly: `curl http://localhost:8080/api/raw-materials`

### Port conflicts

```bash
# Find process using port
lsof -i :8080
lsof -i :3000
lsof -i :5432

# Kill process or use different port
docker-compose down
```

## Maintenance

### Update Dependencies

#### Backend

```bash
cd backend/api
mvn versions:display-dependency-updates
mvn versions:update-properties
```

#### Frontend

```bash
cd frontend
npm outdated
npm update
```

### Database Migrations

```bash
# Hibernatehandles migrations automatically with ddl-auto=update
# For production, use Flyway or Liquibase

# Install Flyway Maven plugin for controlled migrations
mvn flyway:migrate
```

## Security Checklist

- [ ] Database password changed from default
- [ ] Backend CORS configured for specific origins
- [ ] Frontend API URL using HTTPS in production
- [ ] Environment variables not committed to git
- [ ] Regular backups scheduled
- [ ] API rate limiting configured
- [ ] Input validation enabled
- [ ] SQL injection protection verified
- [ ] CSRF tokens configured
- [ ] Secrets Manager integrated (AWS Secrets Manager, Vault, etc.)
